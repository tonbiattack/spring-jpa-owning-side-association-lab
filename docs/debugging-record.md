# デバッグ記録: 双方向関連の逆側だけを更新して外部キーが保存されない

## 実行環境と再現境界

Java 21、Spring Boot 3.4.3、Spring Data JPA、Hibernate、H2を使います。チームを保存して別トランザクションで再読込し、JDBCで`team_member.team_id`を読む統合境界です。

## 再現手順

バグ状態は`14932f4`で`mvn --batch-mode test -Dtest=TeamRepositoryTest`を実行します。修正済み状態は`main`で`mvn --batch-mode clean test`を実行します。

## 最初に観測した事実

`Aki`の子行は保存されますが、再読込チームのメンバーは空で、外部キーを持つ行数は0でした。失敗証跡は`evidence/01-bug-service-test-output.txt`です。

## 競合仮説と検証

| 仮説 | 検証 | 判断 |
| --- | --- | --- |
| 子行が保存されない | `team_member`のnull外部キー行をJDBCで数える | 子行はあり棄却 |
| LAZY取得が原因 | 別トランザクションで関連を読む | 外部キーがnullであり棄却 |
| 所有側未設定が原因 | `TeamMember.team`を設定し再実行する | 関連行が一件となり採用 |

## 確定した原因

双方向関連の所有側がDB関連を更新します。[1] 多対一・一対多では多側が所有側です。[1] 逆側の`Team.members`だけを更新していたため、所有側`TeamMember.team`の外部キーがnullでした。

## 最小修正

`Team#addMember`で新しい`TeamMember`の`assignTeam(this)`を呼び、逆側コレクションへも追加します。

## 回帰保証

### 再発防止テスト

`TeamRepositoryTest`は再読込一覧と外部キー件数を確認します。`OwningSideObservationTest`は所有側設定後の非null外部キーを直接確認します。成功出力は`evidence/03-fixed-full-test-output.txt`です。

## スコープと注意点

新規子エンティティの一対多・多対一関連設定だけを扱います。削除伝播、orphanRemoval、結合取得、複数階層の関連、既存データ修復は対象外です。

## References

[1]: https://jakarta.ee/learn/docs/jakartaee-tutorial/current/persist/persistence-intro/persistence-intro.html "Jakarta EE Tutorial: Introduction to Jakarta Persistence"
