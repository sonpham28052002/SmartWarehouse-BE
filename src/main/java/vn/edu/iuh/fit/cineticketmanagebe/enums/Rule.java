package vn.edu.iuh.fit.cineticketmanagebe.enums;


import lombok.Getter;

@Getter
public enum Rule {
    USER(1), ADMIN(2);
    private final int rule;

    Rule(int rule) {
        this.rule = rule;
    }
}
