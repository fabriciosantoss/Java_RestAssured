package constants;

public enum Permission implements HasName {
    ACCOUNTS_READ("ACCOUNTS_READ"),
    CREDIT_CARD_READ("CREDIT_CARD_READ");

    private final String name;

    Permission(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }
}