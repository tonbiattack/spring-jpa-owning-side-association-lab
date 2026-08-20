package jp.tonbiattack.debuglab.team;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class TeamMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String memberName;

    @ManyToOne
    private Team team;

    protected TeamMember() {
    }

    TeamMember(String memberName) {
        this.memberName = memberName;
    }

    void assignTeam(Team team) {
        this.team = team;
    }

    public String getMemberName() {
        return memberName;
    }
}
