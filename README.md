# 書籍管理システム

- Kotlin, Spring Boot, jOOQでの開発を主題とした書籍管理APIのサンプル実装

## 仕様

- 書籍には著者の属性があり、書籍と著者の情報をRDBに登録・変更・検索ができる
- 著者に紐づく本を取得できる
- APIのみ作成

## その他事項

- logging, カスタムエラーハンドリング, 認証などは実装なし
- テストについてはcontrollerから正常型・異常型を数パターン

## I/F

### 著者検索API

`[GET] /authors`

- リクエストパラメータ

| パラメータ | 説明  | データ型 | 必須 |
|-------|-----|------|----|
| name  | 著者名 | 文字列  |    |

- レスポンス

```json
[
  {
    "id": 1,
    "name": "テスト"
  }
]
```

### 著者登録API

`[POST] /authors`

- リクエストボディ

| パラメータ名 | 説明  | データ型 | 必須 |
|--------|-----|------|----|
| name   | 著者名 | 文字列  | ⭕️ |

- レスポンス

```json
{
  "id": 1,
  "name": "テスト"
}
```

### 著者更新API

`[PATCH] /authors/{id}`

- リクエストボディ

| パラメータ名 | 説明  | データ型 | 必須 |
|--------|-----|------|----|
| name   | 著者名 | 文字列  | ⭕️ |

- レスポンス

```json
{
  "id": 1,
  "name": "テスト"
}
```

### 著者に紐づく書籍取得API

`[GET] /authors/{id}/books`

- レスポンス

```json
[
  {
    "id": 1,
    "title": "タイトル",
    "authorId": 1,
    "publishedAt": "2024-01-01"
  }
]
```

### 書籍検索API

`[GET] /books`

- リクエストパラメータ

| パラメータ名        | 説明    | データ型            | 必須 |
|---------------|-------|-----------------|----|
| title         | タイトル  | 文字列             |    |
| authorName    | 著者名   | 文字列             |    |
| publishedFrom | 出版開始日 | 文字列(yyyy-MM-dd) |    |
| publishedTo   | 出版終了日 | 文字列(yyyy-MM-dd) |    |

- レスポンス

```json
[
  {
    "id": 1,
    "title": "タイトル",
    "authorId": 1,
    "publishedAt": "2024-01-01"
  }
]
```

### 書籍登録API

`[POST] /books`

- リクエストボディ

| パラメータ名      | 説明   | データ型            | 必須 |
|-------------|------|-----------------|----|
| title       | タイトル | 文字列             | ⭕️ |
| authorId    | 著者ID | 数値              | ⭕️ |
| publishedAt | 出版日  | 文字列(yyyy-MM-dd) | ⭕️ |

- レスポンス

```json
{
  "id": 1,
  "title": "タイトル",
  "authorId": 1,
  "publishedAt": "2024-01-01"
}
```

### 書籍更新

`[PUT] /books/{id}`

- リクエストボディ

| パラメータ名      | 説明   | データ型            | 必須 |
|-------------|------|-----------------|----|
| title       | タイトル | 文字列             |    |
| publishedAt | 出版日  | 文字列(yyyy-MM-dd) |    |

- レスポンス

```json
{
  "id": 1,
  "title": "タイトル",
  "authorId": 1,
  "publishedAt": "2024-01-01"
}
```

※ パラメータとして渡された項目のみ更新する

# 環境・構築

## 環境

* Kotlin
* Spring Boot
* Gradle
* JOOQ
* Flyway Migration
* Docker

## 前提

- Dockerインストール済み

## 動作確認

1. コンテナ起動
   ~~~bash
   docker-compose up -d
2. マイグレーション実行
   ~~~bash
   ./gradlew flywayMigrate
   ~~~
3. コード生成
   ~~~bash
   ./gradlew generateJooq
   ~~~
4. ビルド・実行
   ~~~bash
   ./gradlew bootRun
   ~~~
5. 確認
   ~~~bash
   $ curl -X GET http://localhost:8080/books
   ~~~
