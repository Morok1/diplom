package example6;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import to_heroku.model.BugReport;
import to_heroku.service.Validator;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static util.Util.getMethodNodes;

public class WaitAndNotifyValidator implements Validator {
    private static final String VALIDATED_WAIT_NOTIFY = "Validated wait notify!";
    private static final String NOTVALIDATED_WAIT_NOTIFY = "Validated wait notify!";

    @Override
    public void validate(Class<?> clazz, Map<String, List<BugReport>> bugReports) throws IOException {
        String className = clazz.getCanonicalName();
        List<MethodNode> methodNodes = getMethodNodes(className);

//        Map<Boolean, String>  map = methodNodes
//                .stream()
//                .collect(Collectors
//                        .groupingBy(s->checkMethodOnWaitAndNotifyExistence(s).isEmpty(), s -> Collectors.mapping(checkMethodOnWaitAndNotifyExistence(s))));

//        List<>map.get(Boolean.TRUE);

    }

    private String checkMethodOnWaitAndNotifyExistence(MethodNode methodNode){
        AbstractInsnNode[] abstractInsnNodes = methodNode.instructions.toArray();
        for (int i = 0; i < abstractInsnNodes.length ; i++) {
            if(abstractInsnNodes[i].getOpcode() == Opcodes.INVOKEVIRTUAL){
                int var = ((VarInsnNode)abstractInsnNodes[i-1]).var;
                LocalVariableNode localVariableNode  = methodNode.localVariables.get(var);
                return localVariableNode.desc;
            }
        }
        return "";
    }



}
