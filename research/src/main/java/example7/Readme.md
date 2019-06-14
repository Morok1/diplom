#Example7
Не может вызываться this до тех пор, 
пока не проинициализированы все поля.

Смотрим все ли поля проинициализированы

Либо они проинициализированы при  объявлении
 либо через конструктор



```
        L1
         LINENUMBER 9 L1
         ALOAD 0
         DLOAD 1
         PUTFIELD example7/Example7_2.a : D
        L2
         LINENUMBER 10 L2
         ALOAD 0
         ILOAD 3
         PUTFIELD example7/Example7_2.b : I

```
```
      L1
         LINENUMBER 4 L1
         ALOAD 0
         DCONST_1
         PUTFIELD example7/Example7_1.a : D
        L2
         LINENUMBER 5 L2
         ALOAD 0
         SIPUSH 1000
         PUTFIELD example7/Example7_1.b : I
         RETURN

```


