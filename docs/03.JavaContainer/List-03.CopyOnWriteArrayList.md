# CopyOnWriteArrayList源码解读

[TOC]

## CopyOnWriteArrayList简介

CopyOnWriteArrayList是ArrayList的线程安全版本，内部也是通过数组实现，每次对数组的修改都完全拷贝一份新的数组来修改，修改完了再替换掉老数组，这样保证了只阻塞写操作，不阻塞读操作，实现读写分离。

## 继承体系

![](https://raw.githubusercontent.com/hyman213/FigureBed/master/2019/07/20190713222200.png)

CopyOnWriteArrayList实现了List, RandomAccess, Cloneable, java.io.Serializable等接口。

CopyOnWriteArrayList实现了List，提供了基础的添加、删除、遍历等操作。

CopyOnWriteArrayList实现了RandomAccess，提供了随机访问的能力。

CopyOnWriteArrayList实现了Cloneable，可以被克隆。

CopyOnWriteArrayList实现了Serializable，可以被序列化。

## 源码解析

### 属性

```java
// 修改时加锁
final transient ReentrantLock lock = new ReentrantLock();
// 真正存储元素的地方，只能通过getArray()/setArray()访问
private transient volatile Object[] array;
```

（1）lock: 用于修改时加锁，使用transient修饰表示不自动序列化。

（2）array: 真正存储元素的地方，使用transient修饰表示不自动序列化，使用volatile修饰表示一个线程对这个字段的修改另外一个线程立即可见。

### 构造方法

```java
public CopyOnWriteArrayList() {
    // 所有对array的操作都是通过setArray()和getArray()进行
    setArray(new Object[0]);
}

public CopyOnWriteArrayList(Collection<? extends E> c) {
    Object[] elements;
    if (c.getClass() == CopyOnWriteArrayList.class)
        // 如果c也是CopyOnWriteArrayList类型, 那么直接把它的数组拿过来使用
        elements = ((CopyOnWriteArrayList<?>)c).getArray();
    else {
        // 否则调用其toArray()方法将集合元素转化为数组
        elements = c.toArray();
        // c.toArray might (incorrectly) not return Object[] (see 6260652)
        if (elements.getClass() != Object[].class)
            elements = Arrays.copyOf(elements, elements.length, Object[].class);
    }
    setArray(elements);
}

public CopyOnWriteArrayList(E[] toCopyIn) {
    // 把toCopyIn的元素拷贝给当前list的数组。
    setArray(Arrays.copyOf(toCopyIn, toCopyIn.length, Object[].class));
}


final void setArray(Object[] a) {
    array = a;
}
```



### 添加元素

#### add(E e)

```java
public boolean add(E e) {
    final ReentrantLock lock = this.lock;
    // 加锁
    lock.lock();
    try {
        // 获取旧元素
        Object[] elements = getArray();
        int len = elements.length;
        // 旧数组元素拷贝到新数组，新数组大小是旧数组大小加1
        Object[] newElements = Arrays.copyOf(elements, len + 1);
        // 元素放在最后一位
        newElements[len] = e;
        setArray(newElements);
        return true;
    } finally {
        // 释放锁
        lock.unlock();
    }
}
```

#### add(int index, E element)



```java
public void add(int index, E element) {
    final ReentrantLock lock = this.lock;
    lock.lock();
    try {
        Object[] elements = getArray();
        int len = elements.length;
        if (index > len || index < 0)
            throw new IndexOutOfBoundsException("Index: "+index+
                                                ", Size: "+len);
        Object[] newElements;
        int numMoved = len - index;
        if (numMoved == 0)
            // 如果插入的位置是最后一位,那么拷贝一个n+1的数组, 其前n个元素与旧数组一致
            newElements = Arrays.copyOf(elements, len + 1);
        else {
            // 如果插入的位置不是最后一位, 新建一个n+1的数组
            newElements = new Object[len + 1];
            // 拷贝旧数组前index的元素到新数组中
            System.arraycopy(elements, 0, newElements, 0, index);
            // 将index及其之后的元素往后挪一位拷贝到新数组中
            System.arraycopy(elements, index, newElements, index + 1,
                             numMoved);
        }
        // 将元素放置在index处
        newElements[index] = element;
        setArray(newElements);
    } finally {
        // 释放锁
        lock.unlock();
    }
}
```

（1）加锁；

（2）检查索引是否合法，如果不合法抛出IndexOutOfBoundsException异常，注意这里index等于len也是合法的；

（3）如果索引等于数组长度（也就是数组最后一位再加1），那就拷贝一个len+1的数组；

（4）如果索引不等于数组长度，那就新建一个len+1的数组，并按索引位置分成两部分，索引之前（不包含）的部分拷贝到新数组索引之前（不包含）的部分，索引之后（包含）的位置拷贝到新数组索引之后（不包含）的位置，索引所在位置留空；

（5）把索引位置赋值为待添加的元素；

（6）把新数组赋值给当前对象的array属性，覆盖原数组；

（7）解锁；

#### addIfAbsent(E e)

添加一个元素如果这个元素不存在于集合中。

```java
public boolean addIfAbsent(E e) {
    // 获取元素数组-快照
    Object[] snapshot = getArray();
    // 检查如果元素存在,直接返回false;不存在则调用addIfAbsent
    return indexOf(e, snapshot, 0, snapshot.length) >= 0 ? false :
    addIfAbsent(e, snapshot);
}

private boolean addIfAbsent(E e, Object[] snapshot) {
    final ReentrantLock lock = this.lock;
    lock.lock();
    try {
        // 当前元素数组
        Object[] current = getArray();
        int len = current.length;
        // 如果快照与刚获取的数组不一致,说明有修改
        if (snapshot != current) {
            // 重新检查元素是否在刚获取的数组里
            // Optimize for lost race to another addXXX operation
            int common = Math.min(snapshot.length, len);
            for (int i = 0; i < common; i++)
                // 先在0至common索引中寻找
                if (current[i] != snapshot[i] && eq(e, current[i]))
                    return false;
            // common至len索引中寻找
            if (indexOf(e, current, common, len) >= 0)
                return false;
        }
        // 拷贝n+1的数组，将元素放在最后一位
        Object[] newElements = Arrays.copyOf(current, len + 1);
        newElements[len] = e;
        setArray(newElements);
        return true;
    } finally {
        // 释放锁
        lock.unlock();
    }
}
```

（1）检查这个元素是否存在于数组快照中；

（2）如果存在直接返回false，如果不存在调用addIfAbsent(E e, Object[] snapshot)处理;

（3）加锁；

（4）如果当前数组不等于传入的快照，说明有修改，检查待添加的元素是否存在于当前数组中，如果存在直接返回false;

（5）拷贝一个新数组，长度等于原数组长度加1，并把原数组元素拷贝到新数组中；

（6）把新元素添加到数组最后一位；

（7）把新数组赋值给当前对象的array属性，覆盖原数组；

（8）解锁；

### 获取元素

获取指定索引的元素，支持随机访问，时间复杂度为O(1)。

```java
public E get(int index) {
    // 获取元素不需要加锁
    // 直接返回index位置的元素
    // 这里是没有做越界检查的, 因为数组本身会做越界检查
    return get(getArray(), index);
}

final Object[] getArray() {
    return array;
}

private E get(Object[] a, int index) {
    return (E) a[index];
}
```

### 删除元素

```java
public E remove(int index) {
    final ReentrantLock lock = this.lock;
    lock.lock();
    try {
        Object[] elements = getArray();
        int len = elements.length;
        E oldValue = get(elements, index);
        int numMoved = len - index - 1;
        if (numMoved == 0)
            setArray(Arrays.copyOf(elements, len - 1));
        else {
            Object[] newElements = new Object[len - 1];
            System.arraycopy(elements, 0, newElements, 0, index);
            System.arraycopy(elements, index + 1, newElements, index,
                             numMoved);
            setArray(newElements);
        }
        return oldValue;
    } finally {
        lock.unlock();
    }
}
```

（1）加锁；

（2）获取指定索引位置元素的旧值；

（3）如果移除的是最后一位元素，则把原数组的前len-1个元素拷贝到新数组中，并把新数组赋值给当前对象的数组属性；

（4）如果移除的不是最后一位元素，则新建一个len-1长度的数组，并把原数组除了指定索引位置的元素全部拷贝到新数组中，并把新数组赋值给当前对象的数组属性；

（5）解锁并返回旧值；

### 数组长度

返回数组的长度。

```java
public int size() {
    return getArray().length;
}
```

## 总结

（1）CopyOnWriteArrayList使用ReentrantLock重入锁加锁，保证线程安全；

（2）CopyOnWriteArrayList的写操作都要先拷贝一份新数组，在新数组中做修改，修改完了再用新数组替换老数组，所以空间复杂度是O(n)，性能比较低下；

（3）CopyOnWriteArrayList的读操作支持随机访问，时间复杂度为O(1)；

（4）CopyOnWriteArrayList采用读写分离的思想，读操作不加锁，写操作加锁，且写操作占用较大内存空间，所以适用于读多写少的场合；

（5）CopyOnWriteArrayList只保证最终一致性，不保证实时一致性；



为什么CopyOnWriteArrayList没有size属性？

因为每次修改都是拷贝一份正好可以存储目标个数元素的数组，所以不需要size属性了，数组的长度就是集合的大小，而不像ArrayList数组的长度实际是要大于集合的大小的。

比如，add(E e)操作，先拷贝一份n+1个元素的数组，再把新元素放到新数组的最后一位，这时新数组的长度为len+1了，也就是集合的size了。

## 参考

- [彤哥读源码之集合篇](https://mp.weixin.qq.com/s/kpBAIRoMvqPzC-wfLELP3Q)
