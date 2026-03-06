# 📞 通話履歴アプリ — ブラウザだけでビルドする手順

Androidデバイスの着信・発信・不在着信履歴を一覧表示するアプリです。

## 機能

- ✅ 着信履歴の取得・表示
- ✅ 発信・着信・不在着信・拒否を色別アイコンで表示
- ✅ 通話時間・日時を表示
- ✅ 連絡先に登録された名前を表示
- ✅ 権限ダイアログの自動表示

## 表示される情報

| 項目 | 内容 |
|------|------|
| 名前 | 連絡先に登録されていれば名前、なければ番号 |
| 電話番号 | 相手の番号 |
| 種別 | 着信 / 発信 / 不在着信 / 拒否 |
| 日時 | yyyy/MM/dd HH:mm 形式 |
| 通話時間 | 〇分〇秒（不在着信は「-」） |

## セットアップ手順

### 必要な環境
- Android Studio Hedgehog (2023.1.1) 以上
- Android SDK API 24 以上
- Kotlin 1.9.0

### ビルド手順

1. **Android Studio でプロジェクトを開く**
   ```
   File → Open → CallHistoryApp フォルダを選択
   ```

2. **Gradle の同期**
   ```
   File → Sync Project with Gradle Files
   ```

3. **実機またはエミュレーターで実行**
   - Run → Run 'app'
   - ※ エミュレーターには通話履歴がないため、実機推奨

4. **権限の許可**
   - 初回起動時に「通話履歴の読み取り」権限が要求されます
   - 「許可」を選択してください

## プロジェクト構成

```
CallHistoryApp/
├── app/
│   ├── src/main/
│   │   ├── java/com/example/callhistory/
│   │   │   ├── MainActivity.kt        # メイン画面・権限処理
│   │   │   ├── CallLogAdapter.kt      # RecyclerView アダプター
│   │   │   └── CallLogEntry.kt        # データモデル
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   │   ├── activity_main.xml  # メイン画面レイアウト
│   │   │   │   └── item_call_log.xml  # リストアイテム
│   │   │   ├── drawable/              # アイコン・背景
│   │   │   └── values/                # カラー・文字列・テーマ
│   │   └── AndroidManifest.xml
│   └── build.gradle
├── build.gradle
└── settings.gradle
```

## 必要な権限

```xml
<uses-permission android:name="android.permission.READ_CALL_LOG" />
<uses-permission android:name="android.permission.READ_CONTACTS" />
```

## 注意事項

- Android 9 (API 28) 以降では `READ_CALL_LOG` は危険な権限として実行時に許可が必要です
- エミュレーターでテストする場合は通話履歴が存在しないため、実機での確認を推奨します
- Google Play に公開する場合は、`READ_CALL_LOG` 権限の使用理由を申請する必要があります
