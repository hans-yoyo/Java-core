# LinkedList源码解读

[TOC]

## LinkedList简介

LinkedList是一个以双向链表实现的List，**它除了作为List使用，还可以作为队列或者栈来使用**。

## 继承体系

![](https://raw.githubusercontent.com/hyman213/FigureBed/master/2019/07/20190712231213.png)

通过继承体系，我们可以看到LinkedList不仅实现了List接口，还实现了Queue和Deque接口，所以它既能作为List使用，也能作为双端队列使用，当然也可以作为栈使用。

## 源码解析

### 属性

```java
transient int size = 0;
// 链表首节点
transient Node<E> first;
// 链表尾节点
transient Node<E> last;
```

### 内部类Node

典型的双链表结构。

```java
private static class Node<E> {
    E item;
    Node<E> next;
    Node<E> prev;

    Node(Node<E> prev, E element, Node<E> next) {
        this.item = element;
        this.next = next;
        this.prev = prev;
    }
}
```

### 构造方法

```java
public LinkedList() {
}

public LinkedList(Collection<? extends E> c) {
    this();
    addAll(c);
}
```

两个构造方法也很简单，可以看出是一个无界的队列。

### 添加元素

作为一个双端队列，添加元素主要有两种，一种是在队列尾部添加元素，一种是在队列首部添加元素，这两种形式在LinkedList中主要是通过下面两个方法来实现的。

```java
// 从队列首添加元素
private void linkFirst(E e) {
    // 首节点
    final Node<E> f = first;
    // 创建新节点，新节点的next是首节点
    final Node<E> newNode = new Node<>(null, e, f);
    // 新节点作为新的首节点
    first = newNode;
    // 判断是不是第一个添加的元素，如果是就把last也置为新节点，否则把原首节点的prev指针置为新节点
    if (f == null)
        last = newNode;
    else
        f.prev = newNode;
    size++;
    // 修改次数加1，说明这是一个支持fail-fast的集合
    modCount++;
}

// 从队尾添加元素
void linkLast(E e) {
    // 尾节点
    final Node<E> l = last;
    // 创建新节点，新节点的prev 是尾节点
    final Node<E> newNode = new Node<>(l, e, null);
    last = newNode;
    if (l == null)
        first = newNode;
    else
        l.next = newNode;
    size++;
    modCount++;
}

public void addFirst(E e) {
    linkFirst(e);
}

public void addLast(E e) {
    linkLast(e);
}

// Deque操作
// 作为无界队列，添加元素总是会成功的
public boolean offerFirst(E e) {
    addFirst(e);
    return true;
}

public boolean offerLast(E e) {
    addLast(e);
    return true;
}
```

**在中间添加元素**

```java
// 在节点succ之前添加元素
void linkBefore(E e, Node<E> succ) {
    // assert succ != null;
    // succ是待添加节点的后继节点,找到待添加节点的前置节点
    final Node<E> pred = succ.prev;
    // 在其前置节点和后继节点之间创建一个新节点
    final Node<E> newNode = new Node<>(pred, e, succ);
    // 修改后继节点的前置指针指向新节点
    succ.prev = newNode;
    if (pred == null)
        first = newNode;
    else
        pred.next = newNode;
    size++;
    modCount++;
}

// 寻找指定位置的节点
Node<E> node(int index) {
    // assert isElementIndex(index);
    // 因为是双链表
    // 所以根据index是在前半段还是后半段决定从前遍历还是从后遍历    
    // 这样index在后半段的时候可以少遍历一半的元素
    if (index < (size >> 1)) {
        // 如果是在前半段，从前遍历
        Node<E> x = first;
        for (int i = 0; i < index; i++)
            x = x.next;
        return x;
    } else {
        // 从后遍历
        Node<E> x = last;
        for (int i = size - 1; i > index; i--)
            x = x.prev;
        return x;
    }
}

// 在指定位置插入元素
public void add(int index, E element) {
    // 是否越界
    checkPositionIndex(index);
    // 如果index是在队列尾节点之后的一个位置,把新节点直接添加到尾节点之后,否则调用linkBefore()方法在中间添加节点
    if (index == size)
        linkLast(element);
    else
        linkBefore(element, node(index));
}
```

添加元素的三种方式大致如下图所示：

![](https://raw.githubusercontent.com/hyman213/FigureBed/master/2019/07/20190713164447.png)

在队列首尾添加元素很高效，时间复杂度为O(1)。

在中间添加元素比较低效，首先要先找到插入位置的节点，再修改前后节点的指针，时间复杂度为O(n)。

### 删除节点

作为双端队列，删除元素也有两种方式，一种是队列首删除元素，一种是队列尾删除元素。

作为List，又要支持中间删除元素，所以删除元素一个有三个方法，分别如下

```java
// 删除首节点
private E unlinkFirst(Node<E> f) {
    // assert f == first && f != null;
    // 首节点的元素值
    final E element = f.item;
    // 首节点的next指针
    final Node<E> next = f.next;
    f.item = null;
    f.next = null; // help GC
    // 把首节点的next作为新的首节点
    first = next;
    if (next == null)
        last = null;
    else
        next.prev = null;
    size--;
    modCount++;
    // 返回删除的元素
    return element;
}

// 删除尾节点
private E unlinkLast(Node<E> l) {
    // assert l == last && l != null;
    // 尾节点的元素值
    final E element = l.item;
    // 尾节点的前置指针
    final Node<E> prev = l.prev;
    l.item = null;
    l.prev = null; // help GC
    // 重新赋值尾节点
    last = prev;
    if (prev == null)
        first = null;
    else
        prev.next = null;
    size--;
    modCount++;
    return element;
}

// 删除指定节点
E unlink(Node<E> x) {
    // assert x != null;
    final E element = x.item;
    final Node<E> next = x.next;
    final Node<E> prev = x.prev;

    if (prev == null) {
        first = next;
    } else {
        prev.next = next;
        x.prev = null;
    }

    if (next == null) {
        last = prev;
    } else {
        next.prev = prev;
        x.next = null;
    }

    x.item = null;
    size--;
    modCount++;
    return element;
}

// remove的时候如果没有元素抛出异常
public E removeFirst() {
    final Node<E> f = first;
    if (f == null)
        throw new NoSuchElementException();
    return unlinkFirst(f);
}

public E removeLast() {
    final Node<E> l = last;
    if (l == null)
        throw new NoSuchElementException();
    return unlinkLast(l);
}

public E pollFirst() {
    final Node<E> f = first;
    return (f == null) ? null : unlinkFirst(f);
}

public E pollLast() {
    final Node<E> l = last;
    return (l == null) ? null : unlinkLast(l);
}

public E remove(int index) {
    checkElementIndex(index);
    return unlink(node(index));
}
```

删除元素的三种方法都是典型的双链表删除元素的方法，大致流程如下图所示。

![](https://raw.githubusercontent.com/hyman213/FigureBed/master/2019/07/20190713172313.png)

在队列首尾删除元素很高效，时间复杂度为O(1)。

在中间删除元素比较低效，首先要找到删除位置的节点，再修改前后指针，时间复杂度为O(n)

### 作为栈使用

```java
public E pop() {
    return removeFirst();
}

public void push(E e) {
    addFirst(e);
}
```

栈的特性是LIFO(Last In First Out)，所以作为栈使用也很简单，添加删除元素都只操作队列首节点即可。

## 总结

（1）LinkedList是一个以双链表实现的List；

（2）LinkedList还是一个双端队列，具有队列、双端队列、栈的特性；

（3）LinkedList在队列首尾添加、删除元素非常高效，时间复杂度为O(1)；

（4）LinkedList在中间添加、删除元素比较低效，时间复杂度为O(n)；

（5）LinkedList不支持随机访问，所以访问非队列首尾的元素比较低效；

（6）LinkedList在功能上等于ArrayList + ArrayDeque；

## 参考

- [彤哥读源码之集合篇](https://mp.weixin.qq.com/s/kpBAIRoMvqPzC-wfLELP3Q)
