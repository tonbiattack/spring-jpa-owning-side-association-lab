# 新規性レポート: 双方向関連の逆側だけを更新して外部キーが保存されない

既存Qiita原稿にはorphanRemoval、バルク更新、楽観ロック、JPQL null比較、EntityGraphの題材があります。本ラボは、新規子を親コレクションへ追加するときに所有側を設定しないため外部キーがnullになる問題を扱います。

| 軸 | 本ラボ | 既存orphanRemoval題材 |
| --- | --- | --- |
| 直接原因 | 多側の所有側を設定しない | 親から外した子の削除伝播 |
| 実境界 | 新規チーム・メンバーの保存と再読込 | 既存明細の削除 |
| 観測契約 | 再読込一覧と非null外部キー | 子行の削除有無 |
| 最小修正 | `TeamMember.team`を設定する | orphanRemovalの指定・削除操作を整える |

`mappedBy`、所有側、`CascadeType.PERSIST`、`TransientObjectException`、読み取り専用トランザクション語で公開・非公開原稿を検索しました。所有側設定を原因とする同一の再現教材は確認されませんでした。Repository Catalogは存在しないため、Qiita原稿と先行教材による代替監査であることを記録します。

## References

[1]: https://jakarta.ee/learn/docs/jakartaee-tutorial/current/persist/persistence-intro/persistence-intro.html "Jakarta EE Tutorial: Introduction to Jakarta Persistence"
