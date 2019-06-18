package oldapi.example6;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import oldapi.to_heroku.model.BugReport;
import oldapi.service.old.Validator;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static oldapi.util.Util.getMethodNodes;

public class WaitAndNotifyValidator implements Validator {
    private static final String VALIDATED_WAIT_NOTIFY = "Validated wait notify!";
    private static final String NOTVALIDATED_WAIT_NOTIFY = "Validated wait notify!";
    private Logger logger =   Logger.getLogger(WaitAndNotifyValidator.class.getName());


    @Override
    public void validate(Class<?> clazz, Map<String, List<BugReport>> bugReports)  {
        String className = clazz.getCanonicalName();
        try {
            List<MethodNode> methodNodes = getMethodNodes(className);
        } catch (IOException e) {
            logger.info("Some exception {}" + this.getClass().getCanonicalName());
        }

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
