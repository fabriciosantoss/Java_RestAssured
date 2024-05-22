package constants;

public enum Status implements HasName{

    AUTHORISED("AUTHORISED"),
    REJECTED("REJECTED");

    private final String name;

    Status(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
