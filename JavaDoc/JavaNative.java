1. JDK: 
- JVM: Java Virtual Machine -> Thực thi bytecode Java, trên mọi nền tảng = chuyển bytecode java sang mã máy -> viết 1 lần, chạy mọi nơi. (Write Once, Run Anywhere - WORA)
    + Máy ảo: mô phỏng một máy tính chạy trên một máy tính thực, cho phép các ứng dụng Java chạy trên bất kỳ thiết bị nào có JVM mà không cần phải viết lại mã cho mỗi hệ điều hành cụ thể.
    + Độc lập với Nền tảng: JVM giúp ứng dụng Java có thể chạy trên bất kỳ nền tảng nào mà không cần sửa đổi.
    + Thực thi Bytecode: Java app -> compiler -> bytecode -> JVM -> mã máy. JVM biện dịch bytecode thành mã máy = process Just-In-Time (JIT) compilation
    + Quản lý Bộ Nhớ: JVM quản lý bộ nhớ cho các chương trình Java, bao gồm cả việc phân bổ bộ nhớ cho các đối tượng và thu gom rác (garbage collection) để giải phóng bộ nhớ không còn được sử dụng.
    + Bảo mật: JVM cung cấp một lớp bảo mật giữa ứng dụng Java và hệ điều hành máy chủ, giúp ngăn chặn mã độc hại và cung cấp một môi trường thực thi an toàn cho ứng dụng.
    + Tích hợp Thư viện: Cung cấp một tập hợp các thư viện tiêu chuẩn có thể được sử dụng bởi các ứng dụng Java, giúp tăng tốc độ phát triển ứng dụng và giảm thiểu mã cần được viết.

    + Garbage Collection (GC):
      - Đánh dấu và Dọn dẹp (Mark and Sweep)
      - Generational Collection: Bộ nhớ heap được chia thành các khu vực thế hệ khác nhau (ví dụ: Young Generation, Old Generation), và GC sẽ tập trung vào việc dọn dẹp các đối tượng ở "Young Generation" thường xuyên hơn, vì chúng có khả năng chết trẻ hơn.
      - Tối ưu hóa Garbage Collection:
          + Tuning GC: Các tham số JVM có thể được điều chỉnh để tối ưu hóa hoạt động của GC, tùy thuộc vào loại ứng dụng và yêu cầu hiệu suất.
          + Sử dụng các Collector khác nhau: Java cung cấp nhiều loại garbage collectors (ví dụ: Parallel GC, Concurrent Mark Sweep (CMS) GC, Garbage-First (G1) GC), mỗi loại có những ưu và nhược điểm riêng.
      - Quản lý bộ nhớ tự động, Ngăn chặn rò rỉ bộ nhớ, Tăng hiệu suất và ổn định.

    + Class Loader: là một phần của Java Virtual Machine (JVM) chịu trách nhiệm tải các lớp (class) vào bộ nhớ khi chương trình Java chạy.
      - Bootstrap Class Loader: Là class loader cấp cao nhất, chịu trách nhiệm tải các lớp cốt lõi của Java API nằm trong thư viện runtime của Java (rt.jar)
      - Extension Class Loader: Tải các lớp từ các thư viện mở rộng của Java, nằm trong thư mục extensions (jre/lib/ext hoặc bất kỳ thư mục nào được chỉ định bởi hệ thống thuộc tính java.ext.dirs).
      - System/Application Class Loader: Tải các lớp từ classpath của ứng dụng, bao gồm các lớp được định nghĩa bởi người dùng.

      - Cơ chế Delegation Model: Class loader trong Java tuân theo mô hình ủy quyền (Delegation Model), nghĩa là khi một class loader nhận được yêu cầu tải lớp, nó sẽ chuyển yêu cầu đó lên class loader cha trước khi tự mình cố gắng tải lớp. 
                                  Quá trình này tiếp tục cho đến khi đạt đến Bootstrap Class Loader. Nếu lớp không được tìm thấy trong quá trình này, các class loader sẽ theo thứ tự ngược lại cố gắng tải lớp đó.
      - Tùy chỉnh Class Loader: Java cũng cho phép bạn tạo custom class loader bằng cách kế thừa lớp ClassLoader. 
                                Điều này hữu ích khi bạn muốn mở rộng cơ chế tải lớp mặc định của Java, ví dụ như để tải các lớp từ một nguồn không phải là hệ thống file (như mạng, các tập tin được mã hóa, v.v.)

- JRE: Java Runtime Environment, là môi trường thực thi cần thiết để chạy ứng dụng và applet được viết bằng Java, bao gồm: (dành cho user chỉ muốn chạy ứng dụng java mà ko dev)
    + JVM 
    + Standard APIs: bao gồm các công cụ cho UI, xử lý dữ liệu, kết nối mạng, và nhiều chức năng khác.
    + Tệp hỗ trợ thực thi ứng dụng java: tệp cấu hình và các tài nguyên hệ thống.
    + Plugin Cho Trình Duyệt: Trong một số phiên bản JRE, nó bao gồm plugin cho trình duyệt, cho phép các applets Java chạy trực tiếp trong trình duyệt web.

- JDK: Java Development Kit, cung cấp môi trường thực thi cần thiết để phát triển ứng dụng và applet được viết bằng Java, bao gồm:
    + JRE:
    + Java compiler: javac
    + Công cụ đóng gói: jar
    + Công cụ bổ sung: debug, JavaDoc, công cụ phát triển nâng cao

2. JNI: giúp Java gọi code từ ngôn ngữ khác và ngược lại, có tính 2 chiều, nhưng làm mất đặc điểm WORA của java
-


3. Liên kết Java & C++ bằng JNI

3.1. Viết phương thức native trong java Class: 
  - HelloWorld.java
    + private static native String getHelloString(Type params*);
    + static {
          System.loadLibrary("HelloWorld"); // load file HelloWorld.dll 
      }

3.2. Dịch java Class ra file .class = command javac: 
  - "javac HelloWorld.java" -> HelloWorld.class

3.3. Tạo file .h trong C++: 
  - "javac -h . HelloWorld.java" -> HelloWorld.h

  - HelloWorld.h: #include <jni.h>
    + jni.h: in folder "/*/jdk/include/"
      - #include "jni_md.h", <stdio.h>, <stdarg.h>
      - define jint = , jstring = , JNINativeInterface_
    + jni_md.h: in folder "/*/jdk/include/win32"
    + format: JNIEXPORT jType JNICALL Java_package_name_ClassName_methodName(JNIEnv *env, jobject obj, jType params*);

3.4. Viết hàm/lớp/phương thức C++ mà sẽ được gọi từ phương thức native trong Java đã khai báo ở trên:
  - HelloWorld.cpp: #include <HelloWorld.h>
    + method: JNIEXPORT jType JNICALL Java_package_name_ClassName_methodName(JNIEnv *env, jobject obj, jType params*) { // do something }

3.5.  Dịch hàm/lớp/phương thức C++ trên thành file thư viện liên kết động .dll (Windows) hoặc .so (Linux)
  - 2 step: 
    + g++ tao file object:
      - g++ -I "path_file_header" -c <fileName.cpp> -o <fileObject.o>
    + g++ link file object thanh file .dll (.so):
      - g++ -Wl,--add-stdcall-alias -shared -o <tên_file_dll> <tên_file_object>
  
3.5. Load file thư viện đó trong main ở phía Java và sử dụng
  - java -classpath . -Djava.library.path=<đường_dẫn_đến_file_dll> <tên_lớp_main>

3.6. Create Folder "jniLibs" inside "src/main/" -> create sub folders: arm64-v8a, armeabi-v7a, x86 -> Put all your .so libraries inside "src/main/jniLibs/armeabi-v7a, x86" folder
- add to build.gradle/android { 
  sourceSets {
        main {
            jniLibs.srcDirs = ['src/main/jniLibs']
        }
    }
