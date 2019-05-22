package com.test.heroku.testheroku.example2;


import org.objectweb.asm.Opcodes;

import java.util.ArrayList;
import java.util.List;

public class MyPatternFabric {
    public static List<Integer> createPattern(){
        List<Integer> integers = new ArrayList<>();

        integers.add(Opcodes.AALOAD);
        integers.add(Opcodes.GETFIELD);
        integers.add(Opcodes.AALOAD);
        integers.add(Opcodes.GETFIELD);

        return integers;
    }
}
