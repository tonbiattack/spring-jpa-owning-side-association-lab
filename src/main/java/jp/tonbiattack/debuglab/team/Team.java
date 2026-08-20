package jp.tonbiattack.debuglab.team;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String teamCode;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    private List<TeamMember> members = new ArrayList<>();

    protected Team() {
    }

    public Team(String teamCode) {
        this.teamCode = teamCode;
    }

    public void addMember(String memberName) {
        TeamMember member = new TeamMember(memberName);
        member.assignTeam(this);
        members.add(member);
    }

    public Long getId() {
        return id;
    }

    public String getTeamCode() {
        return teamCode;
    }

    public List<TeamMember> getMembers() {
        return members;
    }
}
