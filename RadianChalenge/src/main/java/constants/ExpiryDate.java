package constants;

public enum ExpiryDate implements HasName {

    EXPIRED_DATE("2023-12-20T13:54:31Z"),
    VALID_DATE("2024-12-20T13:54:31Z"),
    INVALID_DATE("28-03-1994");

    private final String name;

    ExpiryDate (String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

}
