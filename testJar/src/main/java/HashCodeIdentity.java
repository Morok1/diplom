public class HashCodeIdentity {
    private Integer a;
    private Integer b;

    public int aInt(){
        return 1;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
