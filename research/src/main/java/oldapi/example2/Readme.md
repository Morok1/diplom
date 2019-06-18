#!= и == 
В зависимости от расположения

1 Паттерн
```
        ALOAD 0
       GETFIELD oldapi.example2/Example2_1.point1 : Lexample2/Example2_1$Point1;
       ALOAD 0
       GETFIELD oldapi.example2/Example2_1.point2 : Lexample2/Example2_1$Point1;
       IF_ACMPEQ L1

```
```
       ALOAD 0
       GETFIELD oldapi.example2/Example2_2.point1 : Lexample2/Point1;
       ALOAD 1
       IF_ACMPNE L2

```

    