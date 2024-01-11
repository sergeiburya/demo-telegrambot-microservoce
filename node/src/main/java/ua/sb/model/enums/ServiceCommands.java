package ua.sb.model.enums;

public enum ServiceCommands {
    HELP("/help"),
    REGISTRATION("/registration"),
    CANCEL("/cancel"),
    START("/start");
    private final String value;

    ServiceCommands(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static ServiceCommands fromValue(String value) {
        for (ServiceCommands v:ServiceCommands.values()) {
            if (v.value.equals(value)) {
                return v;
            }
        }
        return null;
    }
}
