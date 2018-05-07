package cn.xzl.enumdemo;

public enum Constant {
    LOC("loc"),
    TRAVEL("travel"),
    HOTEL("hotel"),
    NETBAR("netbar"),
    TOLLGATE("tollgate");

    private String name;

    private Constant(String val) {
        this.name = val;
    }

    public String getName() {
        return this.name;
    }

    public static Constant getType(String fullName) {
        for (Constant st : values()) {
            if (st.getName().equalsIgnoreCase(fullName)) {
                return st;
            }
        }
        return null;
    }

    public static enum travel{

    }
}
