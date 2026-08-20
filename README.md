# 双方向関連の逆側だけを更新して外部キーが保存されないデバッグラボ

## はじめに

双方向関連では、コレクションに子を追加しただけではDB関連が保存されないことがあります。Jakarta Persistenceでは、双方向関連の所有側がDB上の関連更新を決め、多対一・一対多では多側が所有側です。[1]

## この題材で守る契約

`Team#addMember("Aki")`で追加したメンバーは、チームを再読込したときに取得でき、`team_member.team_id`も保存済みチームを指さなければなりません。

## 最短の開始手順

```bash
mvn --batch-mode clean test
```

修正済み状態では、チーム再読込、物理外部キー、所有側設定の直接観測を含む2統合テストが成功します。

## バグを再現する

```bash
git switch --detach 14932f4
mvn --batch-mode test -Dtest=TeamRepositoryTest
git switch main
```

バグ状態では`Team.members`へ追加するだけで、`TeamMember.team`を設定しません。その結果、再読込したメンバー一覧は空で、`team_member.team_id`はnullになります。証跡は[`evidence/01-bug-service-test-output.txt`](evidence/01-bug-service-test-output.txt)にあります。

## 最小修正

```java
TeamMember member = new TeamMember(memberName);
member.assignTeam(this);
members.add(member);
```

`TeamMember.team`が所有側なので、同じチームを設定します。orphanRemoval、削除伝播、EAGER取得、JSON、N+1は扱いません。

## References

[1]: https://jakarta.ee/learn/docs/jakartaee-tutorial/current/persist/persistence-intro/persistence-intro.html "Jakarta EE Tutorial: Introduction to Jakarta Persistence"
