package tv.ghostvone.sleepingfasternight;

public enum Permission {
    RUN_COMMAND("gfasternight.run_command");

    private String name;

    Permission(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
