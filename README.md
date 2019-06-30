# static_analysis_value_types
Static analysis tool for detecting  object on change by value types
C:\Users\user\Documents\GitHub\valhalla1\target\classes>javap -c -verbose -s Point.class
Classfile /C:/Users/user/Documents/GitHub/valhalla1/target/classes/Point.class
  Last modified 18 ьр  2019 у.; size 1059 bytes
  MD5 checksum 226061926ec358b3381ed3a3733d04ab
  Compiled from "Point.java"
public final value class Point
  minor version: 0
  major version: 52
  flags: (0x0131) ACC_PUBLIC, ACC_FINAL, ACC_SUPER, ACC_VALUE
  this_class: #1                          // Point
  super_class: #6                         // java/lang/Object
  interfaces: 0, fields: 1, methods: 4, attributes: 3
Constant pool:
   #1 = Class              #27            // Point
   #2 = Fieldref           #1.#28         // Point.x:I
   #3 = InvokeDynamic      #0:#31         // #0:hashCode:(QPoint;)I
   #4 = InvokeDynamic      #0:#32         // #0:equals:(QPoint;Ljava/lang/Object;)Z
   #5 = InvokeDynamic      #0:#33         // #0:toString:(QPoint;)Ljava/lang/String;
   #6 = Class              #34            // java/lang/Object
   #7 = Utf8               x
   #8 = Utf8               I
   #9 = Utf8               hashCode
  #10 = Utf8               ()I
  #11 = Utf8               Code
  #12 = Utf8               LineNumberTable
  #13 = Utf8               LocalVariableTable
  #14 = Utf8               this
  #15 = Utf8               QPoint;
  #16 = Utf8               equals
  #17 = Utf8               (Ljava/lang/Object;)Z
  #18 = Utf8               o
  #19 = Utf8               Ljava/lang/Object;
  #20 = Utf8               toString
  #21 = Utf8               ()Ljava/lang/String;
  #22 = Utf8               <init>
  #23 = Utf8               (I)QPoint;
  #24 = Utf8               $value
  #25 = Utf8               SourceFile
  #26 = Utf8               Point.java
  #27 = Utf8               Point
  #28 = NameAndType        #7:#8          // x:I
  #29 = Utf8               BootstrapMethods
  #30 = MethodHandle       6:#35          // REF_invokeStatic java/lang/invoke/ValueBootstrapMethods.makeBootstrapMethod:(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;
  #31 = NameAndType        #9:#36         // hashCode:(QPoint;)I
  #32 = NameAndType        #16:#37        // equals:(QPoint;Ljava/lang/Object;)Z
  #33 = NameAndType        #20:#38
          // toString:(QPoint;)Ljava/lang/String;
  #34 = Utf8               java/lang/Object
  #35 = Methodref          #39.#40        // java/lang/invoke/ValueBootstrapMethods.makeBootstrapMethod:(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;
  #36 = Utf8               (QPoint;)I
  #37 = Utf8               (QPoint;Ljava/lang/Object;)Z
  #38 = Utf8               (QPoint;)Ljava/lang/String;
  #39 = Class              #41            // java/lang/invoke/ValueBootstrapMethods
  #40 = NameAndType        #42:#46        // makeBootstrapMethod:(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;
  #41 = Utf8               java/lang/invoke/ValueBootstrapMethods
  #42 = Utf8               makeBootstrapMethod
  #43 = Class              #48            // java/lang/invoke/MethodHandles$Lookup
  #44 = Utf8               Lookup
  #45 = Utf8               InnerClasses
  #46 = Utf8               (Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;
  #47 = Class              #49            // java/lang/invoke/MethodHandles
  #48 = Utf8               java/lang/invoke/MethodHandles$Lookup
  #49 = Utf8               java/lang/invoke/MethodHandles
{
  final int x;
    descriptor: I
    flags: (0x0010) ACC_FINAL

  public final int hashCode();
    descriptor: ()I
    flags: (0x0011) ACC_PUBLIC, ACC_FINAL
    Code:
      stack=1, locals=1, args_size=1
         0: aload_0
         1: invokedynamic #3,  0              // InvokeDynamic #0:hashCode:(QPoint;)I
         6: ireturn
      LineNumberTable:
        line 2: 0
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0       7     0  this   QPoint;

  public final boolean equals(java.lang.Object);
    descriptor: (Ljava/lang/Object;)Z
    flags: (0x0011) ACC_PUBLIC, ACC_FINAL
    Code:
      stack=2, locals=2, args_size=2
         0: aload_0
         1: aload_1
         2: invokedynamic #4,  0              // InvokeDynamic #0:equals:(QPoint;Ljava/lang/Object;)Z
         7: ireturn
      LineNumberTable:
        line 2: 0
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0       8     0  this   QPoint;
            0       8     1     o   Ljava/lang/Object;

  public final java.lang.String toString();
    descriptor: ()Ljava/lang/String;
    flags: (0x0011) ACC_PUBLIC, ACC_FINAL
    Code:
      stack=1, locals=1, args_size=1
         0: aload_0
         1: invokedynamic #5,  0              // InvokeDynamic #0:toString:(QPoint;)Ljava/lang/String;
         6: areturn
      LineNumberTable:
        line 2: 0
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0       7     0  this   QPoint;

  public static Point Point(int);
    descriptor: (I)QPoint;
    flags: (0x0009) ACC_PUBLIC, ACC_STATIC
    Code:
      stack=2, locals=2, args_size=1
         0: defaultvalue  #1                  // class Point
         3: astore_1
         4: iload_0
         5: aload_1
         6: swap
         7: withfield     #2                  // Field x:I
        10: astore_1
        11: aload_1
        12: areturn
      LineNumberTable:
        line 5: 0
        line 6: 4
        line 7: 11
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0      13     0     x   I
            4       9     1 $value   QPoint;
}

SourceFile: "Point.java"
InnerClasses:
  public static final #44= #43 of #47;    // Lookup=class java/lang/invoke/MethodHandles$Lookup of class java/lang/invoke/MethodHandles
BootstrapMethods:
  0: #30 REF_invokeStatic java/lang/invoke/ValueBootstrapMethods.makeBootstrapMethod:(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;
    Method arguments:


Classfile /C:/Users/user/Documents/GitHub/valhalla1/target/classes/Point.class
  Last modified 18 ьр  2019 у.; size 296 bytes
  MD5 checksum fef5bed6feaa57fd7fd9ba1873a973cb
  Compiled from "Point.java"
public class Point
  minor version: 0
  major version: 52
  flags: (0x0021) ACC_PUBLIC, ACC_SUPER
  this_class: #3                          // Point
  super_class: #4                         // java/lang/Object
  interfaces: 0, fields: 1, methods: 1, attributes: 1
Constant pool:
   #1 = Methodref          #4.#16         // java/lang/Object."<init>":()V
   #2 = Fieldref           #3.#17         // Point.x:I
   #3 = Class              #18            // Point
   #4 = Class              #19            // java/lang/Object
   #5 = Utf8               x
   #6 = Utf8               I
   #7 = Utf8               <init>
   #8 = Utf8               (I)V
   #9 = Utf8               Code
  #10 = Utf8               LineNumberTable
  #11 = Utf8               LocalVariableTable
  #12 = Utf8               this
  #13 = Utf8               LPoint;
  #14 = Utf8               SourceFile
  #15 = Utf8               Point.java
  #16 = NameAndType        #7:#20         // "<init>":()V
  #17 = NameAndType        #5:#6          // x:I
  #18 = Utf8               Point
  #19 = Utf8               java/lang/Object
  #20 = Utf8               ()V
{
  int x;
    descriptor: I
    flags: (0x0000)

  public Point(int);
    descriptor: (I)V
    flags: (0x0001) ACC_PUBLIC
    Code:
      stack=2, locals=2, args_size=2
         0: aload_0
         1: invokespecial #1                  // Method java/lang/Object."<init>":()V
         4: aload_0
         5: iload_1
         6: putfield      #2                  // Field x:I
         9: return
      LineNumberTable:
        line 4: 0
        line 5: 4
        line 6: 9
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0      10     0  this   LPoint;
            0      10     1     x   I
}
SourceFile: "Point.java"
