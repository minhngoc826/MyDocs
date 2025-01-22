// 7.DatabaseContentProvider.java

// https://developer.android.com/guide/topics/providers/content-provider-basics?hl=vi#ContentURIs

1. Tổng quan:
- Chia sẻ quyền truy cập vào dữ liệu ứng dụng của bạn với các ứng dụng khác
- Gửi dữ liệu vào tiện ích
- Trả về nội dung đề xuất tìm kiếm tuỳ chỉnh cho ứng dụng của bạn thông qua khung tìm kiếm bằng SearchRecentSuggestionsProvider
- Đồng bộ hoá dữ liệu ứng dụng với máy chủ của bạn bằng cách triển khai AbstractThreadedSyncAdapter
- Tải dữ liệu trong giao diện người dùng bằng CursorLoader (dùng kèm CursorAdapter và ListView thì yêu cầu 1 cột trong Cursor phải là _ID)

- Quyết định xem bạn có cần một trình cung cấp nội dung hay không. Bạn cần xây dựng một trình cung cấp nội dung nếu muốn cung cấp một hoặc nhiều tính năng sau:
  + Bạn muốn cung cấp dữ liệu hoặc tệp phức tạp cho các ứng dụng khác.
  + Bạn muốn cho phép người dùng sao chép dữ liệu phức tạp trong ứng dụng của mình sang các ứng dụng khác.
  + Bạn muốn cung cấp cụm từ tìm kiếm được đề xuất tuỳ chỉnh bằng khung tìm kiếm.
  + Bạn muốn hiển thị dữ liệu ứng dụng của mình cho các tiện ích.
  + Bạn muốn triển khai các lớp AbstractThreadedSyncAdapter, CursorAdapter hoặc CursorLoader.
  

2. Đọc dữ liệu bởi ứng dụng khác: 
  - Nếu ContentProvider ko define permission nào, các ứng dụng khác sẽ ko có quyền truy cập
  - ContentProvider: define permission read/write by <android:permission="read/write">: quyền này show ra bởi trình quản lý gói Android khi user cài đặt ứng dụng -> user OK thì mới cài.
  - Other apps: declare permission <uses-permission> in manifest 
      + từ API 30: thêm permission QUERY_ALL_PACKAGES + tag <queries> <package android:name="com.example.training"/> </queries>
  - android:protectionLevel="signature": quyền truy cập được kiểm soát nhiều hơn vào dữ liệu của trình cung cấp nội dung khi ứng dụng truy cập vào dữ liệu đã ký bằng cùng một khoá.

  - Quyền cố định cho URI: <path-permission>

  - Truy cập bằng quyền tạm thời cho Uri: 
    khi app ko có quyền vào ContentProvider thì có thể request qua 1 app khác đã được cấp quyền để lấy quyền tạm thời cho Uri, ko phải cho app
    + ContentProvider: define permission for Uri, dùng "android:grantUriPermissions" or thẻ <grant-uri-permission> của <provider>
    + Other apps: sent intent to app có quyền, setFlags FLAG_GRANT_READ_URI_PERMISSION/FLAG_GRANT_WRITE_URI_PERMISSION -> startActivityForResult() -> onActivityResult()
    + Ví dụ: Contacts, send intent với ACTION_PICK, MIME CONTENT_ITEM_TYPE CONTACT = startActivityForResult() -> user select a contact -> trả kết quả trong onActivityResult()
    + grantUriPermissions() / Context.revokeUriPermission()
  
  - Sử dụng ứng dụng khác: kích hoạt app khác có quyền rồi cho user thao tác trong app đó, ví dụ Calendar chấp nhận intent ACTION_INSERT

  
3. Protect data: avoid SQL injection
  - String selectionClause = "var = " + userInput; (var = nothing; DROP TABLE *;) --> String selectionClause =  "var = ?";
  - UriMatcher: match uri -> static mUriMatcher = new UriMatcher()

    
4. Thao tác CRUD
  - Query: return Cursor
  - Insert: return new uri -> get id = ContentUris.parseId(uri)
  - Update: return count updated
  - Delete: return count deleted
  - Truy cập hàng loạt: dùng ContentProviderOperation operators -> ContentResolver.applyBatch(operators): inser, update nhiều row cùng lúc

  - getType: 
    + content://com.example.trains/Line1    --> vnd.android.cursor.dir/vnd.example.line1
    + content://com.example.trains/Line2/5  --> vnd.android.cursor.item/vnd.example.line2
  - getStreamTypes(): Loại MIME cho tệp

  5. Data type
    - primitive data:
    - BLOB: byte[] ~ 64 KB

  6. URI
    + UriMatcher:
    + Uri.Builder:
    + ContentUris:
    + Thuộc tính khởi động và điều khiển
      Những thuộc tính này xác định cách thức và thời điểm hệ thống Android khởi động trình cung cấp, đặc điểm quy trình của trình cung cấp và các chế độ cài đặt thời gian chạy khác:
      android:enabled: gắn cờ cho phép hệ thống khởi động trình cung cấp
      android:exported: gắn cờ cho phép các ứng dụng khác dùng ứng dụng này
      android:initOrder: thứ tự bắt đầu trình cung cấp này, so với các trình cung cấp khác trong cùng một quy trình
      android:multiProcess: gắn cờ cho phép hệ thống khởi động trình cung cấp trong cùng một quy trình với ứng dụng gọi
      android:process: tên của quy trình mà trình cung cấp chạy
      android:syncable: cờ cho biết rằng dữ liệu của nhà cung cấp sẽ được đồng bộ hoá với dữ liệu trên máy chủ
      Các thuộc tính này được ghi lại đầy đủ trong hướng dẫn về phần tử <provider>.
  


