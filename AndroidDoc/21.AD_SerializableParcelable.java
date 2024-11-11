// Serializable vs Parcelable

    /*
    * I. Serializable: bậc thầy của sự đơn giản
    *   - Sử dụng sự ánh xạ (reflection) để suy ra các kiểu DL + object nên tốn time, cả time đọc ghi
    *   - Tạo nhiều Object rác -> tốn time dọn rác
    * II. Parcelable: Vua tốc độ, ~ x10 lần Serializable
    *   - Parcelable cho hiệu năng rất tốt với Android do triển khai rõ ràng về quá trình đọc ghi tuần tự, tuy nhiên nó lại có quá nhiều code thừa.
    *   - Và mỗi khi model thay đổi thì ta sẽ cần phải update lại code parcelable.
    *
    * III. Các class impl Serializable/Parcelable thì subClass cũng phải impl Serializable/Parcelable
    *   - SubClass impl Serializable phải có ít nhất 1 constructor no-args để đảm bảo quá trình Serialization, nếu ko sẽ raise error khi runtime.
    * */

    /* I. Serializable:
    * Serialization:
    *   - Là quá trình chuyển các cấu trúc dữ liệu và các đối tượng thành một định dạng có thể lưu trữ được (vào file, in-memory buffer, hoặc truyền qua network),
    *   - Sau đó có thể phục hồi lại các cấu trúc dữ liệu và đối tượng như ban đầu, trên cùng hoặc khác môi trường.
    *   - sử dụng sự ánh xạ (reflection) để suy ra các kiểu DL + object
    *
    * Thuật toán Serialization sẽ thực hiện các công việc sau:
    *   - Ghi xuống các siêu dữ liệu (metadata) về class (ví dụ như tên của class, version của class, tổng số các field của class,….) của đối tượng đó.
    *   - Ghi đệ quy các thông tin class cho tới khi nó gặp class Object. (các fields)
    *   - Ghi các dữ liệu của các đối tượng
    *
    * Tuần tự hóa có ba mục đích chính sau
    *   - Cơ chế ổn định: Nếu luồng được sử dụng là FileOuputStream, thì dữ liệu sẽ được tự động ghi vào tệp.
    *   - Cơ chế sao chép: Nếu luồng được sử dụng là ByteArrayObjectOuput, thì dữ liệu sẽ được ghi vào một mảng byte trong bộ nhớ.
    *       Mảng byte này sau đó có thể được sử dụng để tạo ra các bản sao của các đối tượng ban đầu.
    *   - Nếu luồng đang được sử dụng xuất phát từ một Socket thì dữ liệu sẽ được tự động gửi đi tới Socket nhận,
    *       khi đó chương trình nhận sẽ quyết định phải làm gì đối với dữ liệu nhận được (giải mã để có được dữ liệu của đối tượng)
    *
    * Tại sao lại cần đến Serialization?
    *   - Một hệ thống enterprise điển hình thường có các thành phần nằm phân tán rải rác trên các hệ thống và mạng khác nhau.
    *       Trong Java mọi thứ đều được miêu tả như là một object. Nếu 2 thành phần Java cần liên lạc với nhau, ta cần phải có một cơ chế để chúng trao đổi dữ liệu.
    *       Serialization được định nghĩa cho mục đích này, và các thành phần Java sẽ sử dụng giao thức (protocol) này để truyền các object qua lại với nhau.
    *   - Có thể dùng trao đối dữ liệu giữa 2 hệ thông khác nhau sử dụng thuật tóan tuần tự hóa mà không phụ thuộc vào nền tảng giữa chúng.
    * */

    /* II. Parcelable:
    *   - writeToParcel(): triển khai ghi tất cả dữ liệu có trong lớp tới Parcel
    *   - Parcelable.Creator: để giải tuần tự tái tạo lại Java Object
    *
    * Java - Parcelable: lib AutoValue (https://github.com/google/auto/blob/main/value/userguide/index.md)
    * Kotlin - Parcelable: @Parcelize - tự động implement Parcelable
    *   Require:
    *   Kotlin 1.4.0
    *   build.gradle:
    *       apply plugin: ‘kotlin-android-extensions’
            androidExtensions {
                experimental = true
            }
    * */

    /* So sánh Parcelable và Serializable
    Serializable:
        Ưu điểm:
            Cực kì dễ triển khai, ít yêu cầu kèm theo
            Có thể ghi đối tượng xuống dạng tệp tin để lưu trữ xuống ổ đĩa, và có thể đọc ở nhìu hệ thống khác nhau
        Nhược điểm:
            Nó có tốc độ chậm, sinh ra nhiều đối tượng rác (garbage )
            Nó rất khó để bảo trì (maintain) nếu bạn thay đổi cấu trúc của class.
    Parcelable:
        Ưu điểm:
            Nó nhanh hơn Serializable
            Dễ dàng đánh phiên bản cho đối tượng
            Kiểm soát được dữ liệu tuần tự
        Nhược điểm:
            Nó phụ thuộc vào nên tảng (hiện tại phương thức này chỉ áp dụng cho android)
            Vì chỉ tồn tại trong vòng đời của Activity nên dữ liệu không được ghi xuống file
            Triển khai khó khăn hơn. Cấu trúc dữ liệu của Object thay đổi là cần thay đổi 1 trình tự đọc và ghi của phương thức.*/

    /*
    * - Serializable according to the docs can also be done in a manual way, using writeObject(ObjectOutputStream) and readObject(ObjectInputStream) methods.
    *   This is how it should be done for the best performance (faster than Parcelable, https://github.com/minhngoc826/Android-Serialization-Test/tree/master)
    *   @Doc:  http://docs.oracle.com/javase/7/docs/api/java/io/Serializable.html
    *   +  writeObject(ObjectOutputStream out): invoke out.defaultWriteObject to writing fields to the stream -> then write primitive data by DataOutput
    *   + readObject(ObjectInputStream in): invoke in.defaultReadObject to read fields from the stream -> then read primitive data by DataInput
    *   + Serializable should define "serialVersionUID" for faster, if not serialization runtime will calculate a default serialVersionUID value for that class based on various aspects of the class.
    *
    *
    * - If serializable is faster and easier to implement, why android has parcelable at all?
    *   The reason is native code. Parcelable is created not just for interprocess communication.
    *   It also can be used for intercode communication. You can send and recieve objects from C++ native layer. That's it.
    * */

    class SerializableExample {

        public void testParcelable() {
            TreeNode root = createNode(0);
            Parcel parcel = Parcel.obtain();

            long start = System.nanoTime();

            root.writeToParcel(parcel, 0);

            long finish = System.nanoTime();

            int length = parcel.marshall().length;
            parcel.setDataPosition(0); // reset for reading

            long start2 = System.nanoTime();
            TreeNode restored = TreeNode.CREATOR.createFromParcel(parcel);

            long finish2 = System.nanoTime();
            System.out.println(restored);
            parcel.recycle();
        }

        public void testSerializable() {
            TreeNode root = createNode(0);

            ObjectOutputStream out = null;
            ByteArrayOutputStream bas = new ByteArrayOutputStream(1_000_000);

            long start = System.nanoTime();
            try {
                out = new ObjectOutputStream(bas);
                out.writeObject(root);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            long finish = System.nanoTime();
            byte[] byteArray = bas.toByteArray();
            int length = byteArray.length;
            ByteArrayInputStream bis = new ByteArrayInputStream(byteArray);
            ObjectInputStream in = null;
            TreeNode restored = null;

            long start2 = System.nanoTime();
            try {
                in = new ObjectInputStream(bis);
                restored = (TreeNode) in.readObject();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            long finish2 = System.nanoTime();
            System.out.println(restored);
        }
    }
