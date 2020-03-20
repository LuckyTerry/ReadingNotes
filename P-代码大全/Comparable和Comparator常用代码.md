# Comparable和Comparator常用代码

> Java中有两种常见的用于排序的接口：Comparable 和 Comparator，现记录下两者的基本使用姿势。

## Comparable

一个类实现了Comparable接口，就意味着该类支持排序，需要重写 compareTo方法

```
public class Animal implements Comparable<Animal> {
 
    private  int age;
    private  String name;
 
    public Animal(int age, String name) {
        this.age = age;
        this.name = name;
    }
       
    @Override
    public int compareTo(Animal o) {
        return this.age - o.age;
    }
}
```

## Comparator

我们如果需要控制某个类的次序，而该类本身不支持排序(即没有实现Comparable接口)，那么我们就可以建立一个“该类的比较器”来进行排序，这个“比较器”只需要实现Comparator接口即可。也就是说，我们可以通过实现Comparator来新建一个比较器，然后通过这个比较器对类进行排序 , 需要重写 compare方法（比较方法中 也可以使用字段的 compareTo 方法辅助）


```
Collections.sort(animalList, new Comparator<Animal>() {
    @Override
    public int compare(Animal o1, Animal o2) {
        return o1.getName().compareTo(o2.getName());
    }
});
```

当然，也可以实现Comparator方法，是的某个类成为一个Comparator

```
public class Animal implements Comparator<Animal> {
 
    private  int age;
    private  String name;
 
    public Animal(int age, String name) {
        this.age = age;
        this.name = name;
    }
    
    @Override
    public int compare(Animal o1, Animal o2) {
        return o1.age - o2.age;
    }
}
```

### 静态方法

- public static <T extends Comparable<? super T>> Comparator<T> reverseOrder()
- public static <T extends Comparable<? super T>> Comparator<T> naturalOrder()
- public static <T> Comparator<T> nullsFirst(Comparator<? super T> comparator)
- public static <T> Comparator<T> nullsLast(Comparator<? super T> comparator)
- public static <T, U> Comparator<T> comparing(Function<? super T, ? extends U> keyExtractor, Comparator<? super U> keyComparator)
- public static <T, U extends Comparable<? super U>> Comparator<T> comparing(Function<? super T, ? extends U> keyExtractor)
- public static <T> Comparator<T> comparingInt(ToIntFunction<? super T> keyExtractor)
- public static <T> Comparator<T> comparingLong(ToLongFunction<? super T> keyExtractor)
- public static<T> Comparator<T> comparingDouble(ToDoubleFunction<? super T> keyExtractor)

### 实例方法

- default Comparator<T> reversed()
- default Comparator<T> thenComparing(Comparator<? super T> other)
- default <U> Comparator<T> thenComparing(Function<? super T, ? extends U> keyExtractor, Comparator<? super U> keyComparator)
- default <U extends Comparable<? super U>> Comparator<T> thenComparing(Function<? super T, ? extends U> keyExtractor)
- default Comparator<T> thenComparingInt(ToIntFunction<? super T> keyExtractor)
- default Comparator<T> thenComparingLong(ToLongFunction<? super T> keyExtractor)
- default Comparator<T> thenComparingDouble(ToDoubleFunction<? super T> keyExtractor)

### 常见场景

自然排序：1, 2, 3
```
System.out.println(Stream.of(2, 3, 1).sorted(Integer::compareTo).collect(toList()));
```

自然排序：1, 2, 3
```
System.out.println(Stream.of(2, 3, 1).sorted(Integer::compare).collect(toList()));
```

自然排序：1, 2, 3
```
System.out.println(Stream.of(2, 3, 1).sorted(naturalOrder()).collect(toList()));
```

逆序排序：3, 2, 1
```
System.out.println(Stream.of(2, 3, 1).sorted(reverseOrder()).collect(toList()));
```

自然排序，Null最后：1, 2, 3
```
System.out.println(Stream.of(2, null, 3, 1).sorted(nullsLast(naturalOrder())).collect(toList()));
```

对某个字段排序：1, 2, 3
```
System.out.println(Stream.of(2, 3, 1).sorted(comparing(Function.identity())).collect(toList()));
```

使用指定的Comparator对某个字段排序：3, 2, 1
```
// 此处Comparator.<Integer>reverseOrder()的<Integer>不能省略，泛型上界会无法推断，导致找不到匹配的方法
System.out.println(Stream.of(2, 3, 1).sorted(comparing(Function.identity(), Comparator.<Integer>reverseOrder())).collect(toList()));
```

来看一个结果令人意外的使用方式
```
// 形参 Function<? super T, ? extends U> keyExtractor，使用Function.identity()的话，U的类型无法推断出来
// 形参 Comparator<? super U>，如果使用Comparator.reverseOrder()的话，U的类型也无法推断出来
System.out.println(Stream.of(2, 3, 1).sorted(comparing(Function.identity(), Comparator.reverseOrder())).collect(toList()));
// 所以，要解决问题的话，需要使其中一个形参对应的实参，应用显示的类型定义
// 为什么平时遇不到这个问题？大概是一般都是用SomeClass::getField的方式，这种方式是显示指定了类型的
System.out.println(Stream.of(2, 3, 1).sorted(comparing(x -> x, Comparator.reverseOrder())).collect(toList()));
System.out.println(Stream.of(2, 3, 1).sorted(comparing(Function.<Integer>identity(), Comparator.reverseOrder())).collect(toList()));
System.out.println(Stream.of(2, 3, 1).sorted(comparing(Function.identity(), Comparator.<Integer>reverseOrder())).collect(toList()));
```

Null值排序值最低，并且使用指定的Comparator对某个字段排序：1, 2, 3, null
```
System.out.println(Stream.of(2, 3, null, 1).sorted(comparing(Function.<Integer>identity(), nullsLast(naturalOrder()))).collect(toList()));
```

## 两者的异同

Comparable和Comparator区别比较

Comparable是排序接口，若一个类实现了Comparable接口，就意味着“该类支持排序”。

Comparator是比较器，我们若需要控制某个类的次序，可以建立一个“该类的比较器”来进行排序。

Comparable相当于“内部比较器”，而Comparator相当于“外部比较器”。

两种方法各有优劣，

用Comparable 简单， 只要实现Comparable 接口的对象直接就成为一个可以比较的对象，但是需要修改源代码。

用Comparator 的好处是不需要修改源代码， 而是另外实现一个比较器， 当某个自定义的对象需要作比较的时候，把比较器和对象一起传递过去就可以比大小了， 并且在Comparator 里面用户可以自己实现复杂的可以通用的逻辑，使其可以匹配一些比较简单的对象，那样就可以节省很多重复劳动了。

另外，补充一个用Comparator构建一个复杂的Comparable接口的实现

```
public interface ItemComparable extends Comparable<ItemComparable> {

    LocalDateTime getUrgedTimeForSort();

    LocalDateTime getCallUpTimeForSort();

    LocalDateTime getPrepareTimeForSort();

    LocalDateTime getHangUpTimeForSort();

    Integer getSplitIndexForSort();

    @Override
    default int compareTo(ItemComparable o) {
        Comparator<ItemComparable> comparator = comparing(ItemComparable::getUrgedTimeForSort, nullsLast(naturalOrder()))
                .thenComparing(ItemComparable::getCallUpTimeForSort, nullsLast(naturalOrder()))
                .thenComparing(ItemComparable::getPrepareTimeForSort, nullsLast(naturalOrder()))
                .thenComparing(ItemComparable::getHangUpTimeForSort, nullsLast(naturalOrder()));
        return comparator.compare(this, o);
    }
}
```