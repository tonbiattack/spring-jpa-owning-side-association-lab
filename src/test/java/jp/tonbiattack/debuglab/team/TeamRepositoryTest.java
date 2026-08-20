package jp.tonbiattack.debuglab.team;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;

@SpringBootTest
class TeamRepositoryTest {

    @Autowired
    private TeamRepository repository;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void persistsMemberRelationWhenAddingMemberToTeam() {
        Long teamId = transactionTemplate.execute(status -> {
            Team team = new Team("team-001");
            team.addMember("Aki");
            return repository.saveAndFlush(team).getId();
        });

        List<String> reloadedMemberNames = transactionTemplate.execute(status -> {
            entityManager.clear();
            return repository.findById(teamId).orElseThrow().getMembers().stream()
                    .map(TeamMember::getMemberName)
                    .toList();
        });
        Integer relatedMemberCount = jdbcTemplate.queryForObject(
                "select count(*) from team_member where team_id = ?", Integer.class, teamId
        );

        assertAll(
                () -> assertEquals(List.of("Aki"), reloadedMemberNames,
                        "チームを再読込すると追加したメンバーを返す"),
                () -> assertEquals(1, relatedMemberCount,
                        "team_memberの外部キーは保存済みチームを指す")
        );
    }
}
