# TreeMap源码解读

[TOC]

## TreeMap简介

TreeMap使用红黑树存储元素，可以保证元素按key值的大小进行遍历。

## 继承体系

![](https://raw.githubusercontent.com/hyman213/FigureBed/master/2019/07/20190714161530.png)

TreeMap实现了Map、SortedMap、NavigableMap、Cloneable、Serializable等接口。

SortedMap规定了元素可以按key的大小来遍历，它定义了一些返回部分map的方法。

```java
public interface SortedMap<K,V> extends Map<K,V> {
    // key的比较器
    Comparator<? super K> comparator();

    // 返回fromKey（包含）到toKey（不包含）之间的元素组成的子map。修改次subMap中的元素，会影响到原map，反之亦然
    SortedMap<K,V> subMap(K fromKey, K toKey);

    // 返回小于toKey（不包含）的子map
    SortedMap<K,V> headMap(K toKey);

    // 返回大于等于fromKey（包含）的子map
    SortedMap<K,V> tailMap(K fromKey);

    // 返回最小的key
    K firstKey();

    // 返回最大的key
    K lastKey();

    // 返回key的集合
    Set<K> keySet();

    // 返回value集合
    Collection<V> values();

    // 返回节点集合
    Set<Map.Entry<K, V>> entrySet();
}
```

NavigableMap是对SortedMap的增强，定义了一些返回离目标key最近的元素的方法。

```java
public interface NavigableMap<K,V> extends SortedMap<K,V> {
    // 小于给定key的最大节点
    Map.Entry<K,V> lowerEntry(K key);

   	// 小于给定key的最大key
    K lowerKey(K key);

    // 小于等于给定key的最大节点
    Map.Entry<K,V> floorEntry(K key);

   // 小于等于给定key的最大key
    K floorKey(K key);

    // 大于等于给定key的最小节点
    Map.Entry<K,V> ceilingEntry(K key);

    // 大于等于给定key的最小key
    K ceilingKey(K key);

    // 大于给定key的最小节点
    Map.Entry<K,V> higherEntry(K key);

    // 大于给定key的最小key
    K higherKey(K key);

    // 最小的节点
    Map.Entry<K,V> firstEntry();

    // 最大的节点
    Map.Entry<K,V> lastEntry();

    // 移除并返回最小的节点
    Map.Entry<K,V> pollFirstEntry();

    // 移除并返回最大的节点
    Map.Entry<K,V> pollLastEntry();

    // 返回倒序的map
    NavigableMap<K,V> descendingMap();

    // 返回有序的key集合
    NavigableSet<K> navigableKeySet();

    // 返回倒序的key集合
    NavigableSet<K> descendingKeySet();

    // 返回从fromKey到toKey的子map，是否包含起止元素可以自己决定
    NavigableMap<K,V> subMap(K fromKey, boolean fromInclusive,
                             K toKey,   boolean toInclusive);

    // 返回小于toKey的子map，是否包含toKey自己决定
    NavigableMap<K,V> headMap(K toKey, boolean inclusive);

    // 返回大于fromKey的子map，是否包含fromKey自己决定
    NavigableMap<K,V> tailMap(K fromKey, boolean inclusive);

    // 等价于subMap(fromKey, true, toKey, false)
    SortedMap<K,V> subMap(K fromKey, K toKey);

    // 等价于headMap(toKey, false)
    SortedMap<K,V> headMap(K toKey);

    // 等价于tailMap(fromKey, true)
    SortedMap<K,V> tailMap(K fromKey);
}
```



## 存储结构

![](https://raw.githubusercontent.com/hyman213/FigureBed/master/2019/07/20190714205718.png)

TreeMap只使用到了红黑树，所以它的时间复杂度为O(log n)，我们再来回顾一下红黑树的特性。

（1）每个节点或者是黑色，或者是红色。

（2）根节点是黑色。

（3）每个叶子节点（NIL）是黑色。（注意：这里叶子节点，是指为空(NIL或NULL)的叶子节点！）

（4）如果一个节点是红色的，则它的子节点必须是黑色的。

（5）从一个节点到该节点的子孙节点的所有路径上包含相同数目的黑节点。



## 源码解析

### 属性

```java
private static final boolean RED   = false;
private static final boolean BLACK = true;
// 比较器，如果没传则key要实现Comparable接口
private final Comparator<? super K> comparator;
// 根节点：根节点，TreeMap没有桶的概念，所有的元素都存储在一颗树中。
private transient Entry<K,V> root;
// 元素个数
private transient int size = 0;
// 修改次数
private transient int modCount = 0;
```

### Entry内部类

存储节点，典型的红黑树结构。

```java
static final class Entry<K,V> implements Map.Entry<K,V> {
    K key;
    V value;
    Entry<K,V> left;
    Entry<K,V> right;
    Entry<K,V> parent;
    boolean color = BLACK;
}
```

### 构造方法

```java
// 默认构造方法，key必须实现Comparable接口 
public TreeMap() {
    comparator = null;
}

// 使用传入的comparator比较两个key的大小
public TreeMap(Comparator<? super K> comparator) {
    this.comparator = comparator;
}

// key必须实现Comparable接口，把传入map中的所有元素保存到新的TreeMap中
public TreeMap(Map<? extends K, ? extends V> m) {
    comparator = null;
    putAll(m);
}

// 使用传入map的比较器，并把传入map中的所有元素保存到新的TreeMap中 
public TreeMap(SortedMap<K, ? extends V> m) {
    comparator = m.comparator();
    try {
        buildFromSorted(m.size(), m.entrySet().iterator(), null, null);
    } catch (java.io.IOException cannotHappen) {
    } catch (ClassNotFoundException cannotHappen) {
    }
}

```

构造方法主要分成两类，一类是使用comparator比较器，一类是key必须实现Comparable接口。

### get(Object key)

```java
public V get(Object key) {
    // 根据key查找元素
    Entry<K,V> p = getEntry(key);
    return (p==null ? null : p.value);
}

final Entry<K,V> getEntry(Object key) {
    // Offload comparator-based version for sake of performance
    // 如果comparator不为空，使用comparator的版本获取元素
    if (comparator != null)
        return getEntryUsingComparator(key);
    if (key == null)
        throw new NullPointerException();
   // 将key强转为Comparable
    @SuppressWarnings("unchecked")
    Comparable<? super K> k = (Comparable<? super K>) key;
    Entry<K,V> p = root;
    while (p != null) {
        int cmp = k.compareTo(p.key);
        if (cmp < 0)
            // 如果小于0从左子树查找
            p = p.left;
        else if (cmp > 0)
            // 如果大于0从右子树查找
            p = p.right;
        else
            return p;
    }
    return null;
}


final Entry<K,V> getEntryUsingComparator(Object key) {
    @SuppressWarnings("unchecked")
    K k = (K) key;
    Comparator<? super K> cpr = comparator;
    if (cpr != null) {
        Entry<K,V> p = root;
        while (p != null) {
            int cmp = cpr.compare(k, p.key);
            if (cmp < 0)
                p = p.left;
            else if (cmp > 0)
                p = p.right;
            else
                return p;
        }
    }
    return null;
}

```

（1）从root遍历整个树；

（2）如果待查找的key比当前遍历的key小，则在其左子树中查找；

（3）如果待查找的key比当前遍历的key大，则在其右子树中查找；

（4）如果待查找的key与当前遍历的key相等，则找到了该元素，直接返回；

（5）从这里可以看出是否有comparator分化成了两个方法，但是内部逻辑一模一样，

### 红黑树左旋、右旋

**红黑树特性**

（1）每个节点或者是黑色，或者是红色。

（2）根节点是黑色。

（3）每个叶子节点（NIL）是黑色。（注意：这里叶子节点，是指为空(NIL或NULL)的叶子节点！）

（4）如果一个节点是红色的，则它的子节点必须是黑色的。

（5）从一个节点到该节点的子孙节点的所有路径上包含相同数目的黑节点。

**左旋**

左旋，就是以某个节点为支点向左旋转。

![](https://raw.githubusercontent.com/hyman213/FigureBed/master/2019/07/20190714214411.png)

整个左旋过程如下：

（1）将 y的左节点 设为 x的右节点，即将 β 设为 x的右节点；

（2）将 x 设为 y的左节点的父节点，即将 β的父节点 设为 x；

（3）将 x的父节点 设为 y的父节点；

（4）如果 x的父节点 为空节点，则将y设置为根节点；如果x是它父节点的左（右）节点，则将y设置为x父节点的左（右）节点；

（5）将 x 设为 y的左节点；

（6）将 x的父节点 设为 y；

```java
// 以p为支点进行左旋，假设p为图中的x
private void rotateLeft(Entry<K,V> p) {
    if (p != null) {
        // p的右节点，即y
        Entry<K,V> r = p.right;
        // （1）将 y的左节点 设为 x的右节点
        p.right = r.left;
        // （2）将 x 设为 y的左节点的父节点（如果y的左节点存在的话）
        if (r.left != null)
            r.left.parent = p;
        // （3）将 x的父节点 设为 y的父节点
        r.parent = p.parent;
        if (p.parent == null)
            // 如果 x的父节点 为空，则将y设置为根节点
            root = r;
        else if (p.parent.left == p)
            // 如果x是它父节点的左节点，则将y设置为x父节点的左节点
            p.parent.left = r;
        else
            // 如果x是它父节点的右节点，则将y设置为x父节点的右节点
            p.parent.right = r;
        // （5）将 x 设为 y的左节点
        r.left = p;
        // （6）将 x的父节点 设为 y
        p.parent = r;
    }
}
```

**右旋**

右旋，就是以某个节点为支点向右旋转。

![](https://raw.githubusercontent.com/hyman213/FigureBed/master/2019/07/20190714214304.png)

整个右旋过程如下：

（1）将 x的右节点 设为 y的左节点，即 将 β 设为 y的左节点；

（2）将 y 设为 x的右节点的父节点，即 将 β的父节点 设为 y；

（3）将 y的父节点 设为 x的父节点；

（4）如果 y的父节点 是 空节点，则将x设为根节点；如果y是它父节点的左（右）节点，则将x设为y的父节点的左（右）节点；

（5）将 y 设为 x的右节点；

（6）将 y的父节点 设为 x；

TreeMap中的实现

```java
private void rotateRight(Entry<K,V> p) {
    if (p != null) {
        Entry<K,V> l = p.left;
        p.left = l.right;
        if (l.right != null) l.right.parent = p;
        l.parent = p.parent;
        if (p.parent == null)
            root = l;
        else if (p.parent.right == p)
            p.parent.right = l;
        else p.parent.left = l;
        l.right = p;
        p.parent = l;
    }
}
```

### put(K key, V value)

```java
public V put(K key, V value) {
    Entry<K,V> t = root;
    if (t == null) {
        // 如果没有根节点，直接插入到根节点
        compare(key, key); // type (and possibly null) check

        root = new Entry<>(key, value, null);
        size = 1;
        modCount++;
        return null;
    }
    // key比较的结果
    int cmp;
    // 用来寻找待插入节点的父节点
    Entry<K,V> parent;
    // 根据是否有comparator使用不同的分支
    Comparator<? super K> cpr = comparator;
    if (cpr != null) {
        // 使用的是comparator方式，key值可以为null，只要在comparator.compare()中允许即可
        do {
            parent = t;
            cmp = cpr.compare(key, t.key);
            if (cmp < 0)
                t = t.left;
            else if (cmp > 0)
                t = t.right;
            else
                return t.setValue(value);
        } while (t != null);
    }
    else {
        // 如果使用的是Comparable方式，key不能为null
        if (key == null)
            throw new NullPointerException();
        @SuppressWarnings("unchecked")
        Comparable<? super K> k = (Comparable<? super K>) key;
        do {
            parent = t;
            cmp = k.compareTo(t.key);
            if (cmp < 0)
                t = t.left;
            else if (cmp > 0)
                t = t.right;
            else
                return t.setValue(value);
        } while (t != null);
    }
    // 如果没找到，那么新建一个节点，并插入到树中
    Entry<K,V> e = new Entry<>(key, value, parent);
    if (cmp < 0)
        // 如果小于0插入到左子节点
        parent.left = e;
    else
        // 如果大于0插入到右子节点
        parent.right = e;
    // 插入之后的平衡
    fixAfterInsertion(e);
    // 元素个数加1（不需要扩容）
    size++;
    modCount++;
    // 如果插入了新节点返回空
    return null;
}
```

### 插入再平衡

插入的元素默认都是红色，因为插入红色元素只违背了第4条特性，那么我们只要根据这个特性来平衡就容易多了。

根据不同的情况有以下几种处理方式：

1. 插入的元素如果是根节点，则直接涂成黑色即可，不用平衡；
2. 插入的元素的父节点如果为黑色，不需要平衡；
3. 插入的元素的父节点如果为红色，则违背了特性4，需要平衡，平衡时又分成下面三种情况：

**（如果父节点是祖父节点的左节点）**

| 情况                                                         | 策略                                                         |
| :----------------------------------------------------------- | :----------------------------------------------------------- |
| 1）父节点为红色，叔叔节点也为红色                            | （1）将父节点设为黑色； （2）将叔叔节点设为黑色； （3）将祖父节点设为红色； （4）将祖父节点设为新的当前节点，进入下一次循环判断； |
| 2）父节点为红色，叔叔节点为黑色，且当前节点是其父节点的右节点 | （1）将父节点作为新的当前节点； （2）以新当节点为支点进行左旋，进入情况3）； |
| 3）父节点为红色，叔叔节点为黑色，且当前节点是其父节点的左节点 | （1）将父节点设为黑色； （2）将祖父节点设为红色； （3）以祖父节点为支点进行右旋，进入下一次循环判断； |

**（如果父节点是祖父节点的右节点，则正好与上面反过来）**

| 情况                                                         | 策略                                                         |
| :----------------------------------------------------------- | :----------------------------------------------------------- |
| 1）父节点为红色，叔叔节点也为红色                            | （1）将父节点设为黑色； （2）将叔叔节点设为黑色； （3）将祖父节点设为红色； （4）将祖父节点设为新的当前节点，进入下一次循环判断； |
| 2）父节点为红色，叔叔节点为黑色，且当前节点是其父节点的左节点 | （1）将父节点作为新的当前节点； （2）以新当节点为支点进行右旋； |
| 3）父节点为红色，叔叔节点为黑色，且当前节点是其父节点的右节点 | （1）将父节点设为黑色； （2）将祖父节点设为红色； （3）以祖父节点为支点进行左旋，进入下一次循环判断； |

让我们来看看TreeMap中的实现：

```java
/**
 * 插入再平衡
 *（1）每个节点或者是黑色，或者是红色。
 *（2）根节点是黑色。
 *（3）每个叶子节点（NIL）是黑色。（注意：这里叶子节点，是指为空(NIL或NULL)的叶子节点！）
 *（4）如果一个节点是红色的，则它的子节点必须是黑色的。
 *（5）从一个节点到该节点的子孙节点的所有路径上包含相同数目的黑节点。
 */
private void fixAfterInsertion(Entry<K,V> x) {
    // 插入的节点为红节点，x为当前节点
    x.color = RED;
    // 只有当插入节点不是根节点且其父节点为红色时才需要平衡（违背了特性4）
    while (x != null && x != root && x.parent.color == RED) {
        if (parentOf(x) == leftOf(parentOf(parentOf(x)))) {
            Entry<K,V> y = rightOf(parentOf(parentOf(x)));
            if (colorOf(y) == RED) {
                setColor(parentOf(x), BLACK);
                setColor(y, BLACK);
                setColor(parentOf(parentOf(x)), RED);
                x = parentOf(parentOf(x));
            } else {
                if (x == rightOf(parentOf(x))) {
                    x = parentOf(x);
                    rotateLeft(x);
                }
                setColor(parentOf(x), BLACK);
                setColor(parentOf(parentOf(x)), RED);
                rotateRight(parentOf(parentOf(x)));
            }
        } else {
            Entry<K,V> y = leftOf(parentOf(parentOf(x)));
            if (colorOf(y) == RED) {
                setColor(parentOf(x), BLACK);
                setColor(y, BLACK);
                setColor(parentOf(parentOf(x)), RED);
                x = parentOf(parentOf(x));
            } else {
                if (x == leftOf(parentOf(x))) {
                    x = parentOf(x);
                    rotateRight(x);
                }
                setColor(parentOf(x), BLACK);
                setColor(parentOf(parentOf(x)), RED);
                rotateLeft(parentOf(parentOf(x)));
            }
        }
    }
    root.color = BLACK;
}
```

... 未完待续

### 删除元素 remove(Object key)

```java
public V remove(Object key) {
    Entry<K,V> p = getEntry(key);
    if (p == null)
        return null;

    V oldValue = p.value;
    deleteEntry(p);
    // 返回原值
    return oldValue;
}


private void deleteEntry(Entry<K,V> p) {
    modCount++;
    size--;

    // If strictly internal, copy successor's element to p and then make p
    // point to successor.
    // 如果当前节点既有左子节点，又有右子节点
    if (p.left != null && p.right != null) {
        // 取其右子树中最小的节点
        Entry<K,V> s = successor(p);
        // 用右子树中最小节点的值替换当前节点的值
        p.key = s.key;
        p.value = s.value;
        p = s;
        // 这种情况实际上并没有删除p节点，而是把p节点的值改了，实际删除的是p的后继节点
    } // p has 2 children

    // 如果原来的当前节点（p）有2个子节点，则当前节点已经变成原来p的右子树中的最小节点了，也就是说其没有左子节点了
    // 到这一步，p肯定只有一个子节点了
    // 如果当前节点有子节点，则用子节点替换当前节点
    Entry<K,V> replacement = (p.left != null ? p.left : p.right);

    if (replacement != null) {
        // 把替换节点直接放到当前节点的位置上（相当于删除了p，并把替换节点移动过来了）
        replacement.parent = p.parent;
        if (p.parent == null)
            root = replacement;
        else if (p == p.parent.left)
            p.parent.left  = replacement;
        else
            p.parent.right = replacement;

        // 将p的各项属性都设为空
        // Null out links so they are OK to use by fixAfterDeletion.
        p.left = p.right = p.parent = null;

        // 如果p是黑节点，则需要再平衡
        if (p.color == BLACK)
            fixAfterDeletion(replacement);
    } else if (p.parent == null) { // return if we are the only node.
        // 如果当前节点就是根节点，则直接将根节点设为空即可
        root = null;
    } else { //  No children. Use self as phantom replacement and unlink.
        // 如果当前节点没有子节点且其为黑节点，则把自己当作虚拟的替换节点进行再平衡
        if (p.color == BLACK)
            fixAfterDeletion(p);

        // 平衡完成后删除当前节点（与父节点断绝关系）
        if (p.parent != null) {
            if (p == p.parent.left)
                p.parent.left = null;
            else if (p == p.parent.right)
                p.parent.right = null;
            p.parent = null;
        }
    }
}
```

### 删除再平衡

经过上面的处理，真正删除的肯定是黑色节点才会进入到再平衡阶段。

因为删除的是黑色节点，导致整颗树不平衡了，所以这里我们假设把删除的黑色赋予当前节点，这样当前节点除了它自已的颜色还多了一个黑色，那么：

（1）如果当前节点是根节点，则直接涂黑即可，不需要再平衡；

（2）如果当前节点是红+黑节点，则直接涂黑即可，不需要平衡；

（3）如果当前节点是黑+黑节点，则我们只要通过旋转把这个多出来的黑色不断的向上传递到一个红色节点即可，这又可能会出现以下四种情况：

**（假设当前节点为父节点的左子节点）**

| 情况                                                         | 策略                                                         |
| :----------------------------------------------------------- | :----------------------------------------------------------- |
| 1）x是黑+黑节点，x的兄弟是红节点                             | （1）将兄弟节点设为黑色； （2）将父节点设为红色； （3）以父节点为支点进行左旋； （4）重新设置x的兄弟节点，进入下一步； |
| 2）x是黑+黑节点，x的兄弟是黑节点，且兄弟节点的两个子节点都是黑色 | （1）将兄弟节点设置为红色； （2）将x的父节点作为新的当前节点，进入下一次循环； |
| 3）x是黑+黑节点，x的兄弟是黑节点，且兄弟节点的右子节点为黑色，左子节点为红色 | （1）将兄弟节点的左子节点设为黑色； （2）将兄弟节点设为红色； （3）以兄弟节点为支点进行右旋； （4）重新设置x的兄弟节点，进入下一步； |
| 3）x是黑+黑节点，x的兄弟是黑节点，且兄弟节点的右子节点为红色，左子节点任意颜色 | （1）将兄弟节点的颜色设为父节点的颜色； （2）将父节点设为黑色； （3）将兄弟节点的右子节点设为黑色； （4）以父节点为支点进行左旋； （5）将root作为新的当前节点（退出循环）； |

**（假设当前节点为父节点的右子节点，正好反过来）**

| 情况                                                         | 策略                                                         |
| :----------------------------------------------------------- | :----------------------------------------------------------- |
| 1）x是黑+黑节点，x的兄弟是红节点                             | （1）将兄弟节点设为黑色； （2）将父节点设为红色； （3）以父节点为支点进行右旋； （4）重新设置x的兄弟节点，进入下一步； |
| 2）x是黑+黑节点，x的兄弟是黑节点，且兄弟节点的两个子节点都是黑色 | （1）将兄弟节点设置为红色； （2）将x的父节点作为新的当前节点，进入下一次循环； |
| 3）x是黑+黑节点，x的兄弟是黑节点，且兄弟节点的左子节点为黑色，右子节点为红色 | （1）将兄弟节点的右子节点设为黑色； （2）将兄弟节点设为红色； （3）以兄弟节点为支点进行左旋； （4）重新设置x的兄弟节点，进入下一步； |
| 3）x是黑+黑节点，x的兄弟是黑节点，且兄弟节点的左子节点为红色，右子节点任意颜色 | （1）将兄弟节点的颜色设为父节点的颜色； （2）将父节点设为黑色； （3）将兄弟节点的左子节点设为黑色； （4）以父节点为支点进行右旋； （5）将root作为新的当前节点（退出循环）； |

让我们来看看TreeMap中的实现：

```java
/**
 * 删除再平衡
 *（1）每个节点或者是黑色，或者是红色。
 *（2）根节点是黑色。
 *（3）每个叶子节点（NIL）是黑色。（注意：这里叶子节点，是指为空(NIL或NULL)的叶子节点！）
 *（4）如果一个节点是红色的，则它的子节点必须是黑色的。
 *（5）从一个节点到该节点的子孙节点的所有路径上包含相同数目的黑节点。
 */
private void fixAfterDeletion(Entry<K,V> x) {
    while (x != root && colorOf(x) == BLACK) {
        if (x == leftOf(parentOf(x))) {
            Entry<K,V> sib = rightOf(parentOf(x));

            if (colorOf(sib) == RED) {
                setColor(sib, BLACK);
                setColor(parentOf(x), RED);
                rotateLeft(parentOf(x));
                sib = rightOf(parentOf(x));
            }

            if (colorOf(leftOf(sib))  == BLACK &&
                colorOf(rightOf(sib)) == BLACK) {
                setColor(sib, RED);
                x = parentOf(x);
            } else {
                if (colorOf(rightOf(sib)) == BLACK) {
                    setColor(leftOf(sib), BLACK);
                    setColor(sib, RED);
                    rotateRight(sib);
                    sib = rightOf(parentOf(x));
                }
                setColor(sib, colorOf(parentOf(x)));
                setColor(parentOf(x), BLACK);
                setColor(rightOf(sib), BLACK);
                rotateLeft(parentOf(x));
                x = root;
            }
        } else { // symmetric
            Entry<K,V> sib = leftOf(parentOf(x));

            if (colorOf(sib) == RED) {
                setColor(sib, BLACK);
                setColor(parentOf(x), RED);
                rotateRight(parentOf(x));
                sib = leftOf(parentOf(x));
            }

            if (colorOf(rightOf(sib)) == BLACK &&
                colorOf(leftOf(sib)) == BLACK) {
                setColor(sib, RED);
                x = parentOf(x);
            } else {
                if (colorOf(leftOf(sib)) == BLACK) {
                    setColor(rightOf(sib), BLACK);
                    setColor(sib, RED);
                    rotateLeft(sib);
                    sib = leftOf(parentOf(x));
                }
                setColor(sib, colorOf(parentOf(x)));
                setColor(parentOf(x), BLACK);
                setColor(leftOf(sib), BLACK);
                rotateRight(parentOf(x));
                x = root;
            }
        }
    }

    setColor(x, BLACK);
}
```

未完待续...

### 循环遍历

```java
@Override
public void forEach(BiConsumer<? super K, ? super V> action) {
    Objects.requireNonNull(action);
    // 遍历前修改的次数
    int expectedModCount = modCount;
    // 执行遍历，先获取第一个元素的位置，再循环遍历后继节点
    for (Entry<K, V> e = getFirstEntry(); e != null; e = successor(e)) {
        // 执行动作
        action.accept(e.key, e.value);

        if (expectedModCount != modCount) {
            throw new ConcurrentModificationException();
        }
    }
}
```

1）寻找第一个节点；

从根节点开始找最左边的节点，即最小的元素。

```java
final Entry<K,V> getFirstEntry() {
    Entry<K,V> p = root;
    // 从根节点开始找最左边的节点，即最小的元素
    if (p != null)
        while (p.left != null)
            p = p.left;
    return p;
}
```

（2）循环遍历后继节点；

```java
static <K,V> TreeMap.Entry<K,V> successor(Entry<K,V> t) {
    if (t == null)
        // 如果当前节点为空，返回空
        return null;
    else if (t.right != null) {
        // 如果当前节点有右子树，取右子树中最小的节点
        Entry<K,V> p = t.right;
        while (p.left != null)
            p = p.left;
        return p;
    } else {
        // 如果当前节点没有右子树
        // 如果当前节点是父节点的左子节点，直接返回父节点
        // 如果当前节点是父节点的右子节点，一直往上找，直到找到一个祖先节点是其父节点的左子节点为止，返回这个祖先节点的父节点
        Entry<K,V> p = t.parent;
        Entry<K,V> ch = t;
        while (p != null && ch == p.right) {
            ch = p;
            p = p.parent;
        }
        return p;
    }
}
```

让我们一起来分析下这种方式的时间复杂度吧。

首先，寻找第一个元素，因为红黑树是接近平衡的二叉树，所以找最小的节点，相当于是从顶到底了，时间复杂度为O(log n)；

其次，寻找后继节点，因为红黑树插入元素的时候会自动平衡，最坏的情况就是寻找右子树中最小的节点，时间复杂度为O(log k)，k为右子树元素个数；

最后，需要遍历所有元素，时间复杂度为O(n)；

所以，总的时间复杂度为 O(log n) + O(n * log k) ≈ O(n)。

虽然遍历红黑树的时间复杂度是O(n)，但是它实际是要比跳表要慢一点的，啥？跳表是啥？安心，后面会讲到跳表的。



## 总结

到这里红黑树就整个讲完了，让我们再回顾下红黑树的特性：

（1）每个节点或者是黑色，或者是红色。

（2）根节点是黑色。

（3）每个叶子节点（NIL）是黑色。（注意：这里叶子节点，是指为空(NIL或NULL)的叶子节点！）

（4）如果一个节点是红色的，则它的子节点必须是黑色的。

（5）从一个节点到该节点的子孙节点的所有路径上包含相同数目的黑节点。

除了上述这些标准的红黑树的特性，你还能讲出来哪些TreeMap的特性呢？

（1）TreeMap的存储结构只有一颗红黑树；

（2）TreeMap中的元素是有序的，按key的顺序排列；

（3）TreeMap比HashMap要慢一些，因为HashMap前面还做了一层桶，寻找元素要快很多；

（4）TreeMap没有扩容的概念；

（5）TreeMap的遍历不是采用传统的递归式遍历；

（6）TreeMap可以按范围查找元素，查找最近的元素；

## 参考

- [彤哥读源码之集合篇](https://mp.weixin.qq.com/s/kpBAIRoMvqPzC-wfLELP3Q)
