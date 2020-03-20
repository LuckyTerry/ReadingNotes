# Java注解

# Annotation

## 简介

## 方法

**Class<? extends Annotation> annotationType();**

返回此注释的注释类型

# AnnotatedElement

## 简介
这个接口（AnnotatedElement）的对象代表了在当前JVM中的一个“被注解元素”（可以是Class，Method，Field，Constructor，Package等）。

在Java语言中，所有实现了这个接口的“元素”都是可以“被注解的元素”。使用这个接口中声明的方法可以读取（通过Java的反射机制）“被注解元素”的注解。这个接口中的所有方法返回的注解都是不可变的、并且都是可序列化的。这个接口中所有方法返回的数组可以被调用者修改，而不会影响其返回给其他调用者的数组。

## 子接口
1. AnnotatedArrayType （被注解的数组类型）
2. AnnotatedParameterizedType （被注解的参数化类型）
3. AnnotatedType （被注解的类型）
4. AnnotatedTypeVariable （被注解的类型变量）
5. AnnotatedWildcardType （被注解的通配类型）
6. GenericDeclaration （通用声明，用于表示声明型元素，如：类、方法、构造器等）
7. TypeVariable<D> （类型变量）

## 实现类
1. AccessibleObject（可访问对象，如：方法、构造器、属性等）
2. Class（类，就是你用Java语言编程时每天都要写的那个东西）
3. Constructor（构造器，类的构造方法的类型）
4. Executable（可执行的，如构造器和方法）
5. Field（属性，类中属性的类型）
6. Method（方法，类中方法的类型）
7. Package（包，你每天都在声明的包的类型）
8. Parameter（参数，主要指方法或函数的参数，其实是这些参数的类型）

getAnnotationsByType(Class)和getDeclaredAnnotationsByType(Class)方法在一个元素上支持多个相同类型的注释。

## 方法

**default boolean isAnnotationPresent(Class<? extends Annotation> annotationClass)**

如果指定类型的注解出现在当前元素上，则返回true，否则将返回false。这种方法主要是为了方便地访问一些已知的注解。

**<T extends Annotation> T getAnnotation(Class<T> annotationClass)**

如果在当前元素上存在参数所指定类型（annotationClass）的注解，则返回对应的注解，否则将返回null。

**Annotation[] getAnnotations()**

返回在这个元素上的所有注解。如果该元素没有注释，则返回值是长度为0的数组。该方法的调用者可以自由地修改返回的数组;它不会对返回给其他调用者的数组产生影响。

**default <T extends Annotation> T[] getAnnotationsByType(Class<T> annotationClass)**

返回与该元素相关联的注解。如果没有与此元素相关联的注解，则返回值是长度为0的数组。这个方法与getAnnotation(Class)的区别在于，该方法检测其参数是否为可重复的注解类型(JLS 9.6)，如果是，则尝试通过“looking through”容器注解来查找该类型的一个或多个注解。该方法的调用者可以自由地修改返回的数组;它不会对返回给其他调用者的数组产生影响。参考@Repeatable。

**default <T extends Annotation> T getDeclaredAnnotation(Class<T> annotationClass)**

如果参数中所指定类型的注解是直接存在于当前元素上的，则返回对应的注解，否则将返回null。这个方法忽略了继承的注解。(如果没有直接在此元素上显示注释，则返回null。)
 
**Annotation[] getDeclaredAnnotations()**

返回直接出现在这个元素上的注解。这种方法忽略了继承的注解。如果在此元素上没有直接存在的注解，则返回值是长度为0的数组。该方法的调用者可以自由地修改返回的数组;它不会对返回给其他调用者的数组产生影响。
 
**default <T extends Annotation> T[] getDeclaredAnnotationsByType(Class<T> annotationClass)**

如果参数中所指定类型的注解是直接存在或间接存在于当前元素上的，则返回对应的注解。这种方法忽略了继承的注释。如果没有直接或间接地存在于此元素上的指定注解，则返回值是长度为0的数组。这个方法和getDeclaredAnnotation(Class)的区别在于，这个方法检测它的参数是否为可重复的注释类型(JLS 9.6)，如果是，则尝试通过“looking through”容器注解来查找该类型的一个或多个注解。该方法的调用者可以自由地修改返回的数组;它不会对返回给其他调用者的数组产生影响。参考@Repeatable。

# AnnotationUtils和AnnotatedElementUtils

## 查找维度

查找算法(for class or methods) 维度
- get：只查找当前类的指定注解
- find：可以查找类继承结构上的指定注解

元注解 维度
- get：递归，仅支持单级元注解
- find：递归，可支持任意级元注解

属性合并 维度
- AnnotationUtils：递归查询到元注解时，不会将派生类注解的属性合并到元注解中
- AnnotatedElementUtils：递归查询到元注解时，会将派生类注解的属性合并到元注解中

## 查找优先级

- 当前类上基于注解递归查找元注解
- 若在当前类未找到，则沿着类继承结构向上反复执行步骤一

## 测试用例

具体的测试用例可见 [spring注解工具类AnnotatedElementUtils和AnnotationUtils](https://www.cnblogs.com/hujunzheng/p/9790588.html)

# Reference

[AnnotatedElement](https://www.jianshu.com/p/953e26463fbc)