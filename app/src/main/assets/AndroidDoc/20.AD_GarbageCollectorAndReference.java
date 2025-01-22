// 8.GarbageCollectorAndReference.md

// 1. Garbage Collector
//   - Là chương trình chạy nền, theo dõi các đối tượng trong bộ nhớ, và tiến hành thu hồi bộ nhớ (quá trình Garbage Collection) của các đối tượng khi chúng không còn được tham chiếu đến.
//   - 
// 2. Reference: Một tham chiếu chỉ đến một đối tượng được khai báo, nhờ đó ta có thể truy cập được nó.
//   - Strong reference: default, được tạo tự động khi new object, GC ko thể thu hồi nếu chưa remove tham chiếu (destroy or gán = null) hoặc đang còn strong tham chiếu phụ của obj khác đến nó
//   - Weak reference: custom, GC có thể bỏ qua weak reference và thu hồi ngay obj khi remove tham chiếu của obj. các đối tượng weakly reachable object có thể truy cập qua method get() nếu như nó vẫn còn trong bộ nhớ.
//   - Soft reference: giống weak nhưng GC ko thu hồi ngay mà cho vào ReferenceQueue và được thu hồi khi GC cần, GC đảm bảo thu hồi obj soft reference trc khi bị OOM, ưu tiên giữ lại những tham chiếu là recently-created hoặc recently-used. 
//   - Phantom reference:

// 1. Strong reference: Activity vs AsyncTask thường bị leak mem khi activity bị destroy mà AsyncTask đang chạy.
public class StrongRefDemo {
    static class MyObject {
        @ Override
        protected void finalize() throws Throwable {
            super.finalize();
            System.out.println("I'm collected!"); // will be printed if  MyObject is collected by GC
        }
    }
    static MyObject obj;
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Start");
        obj = new MyObject(); // New strong reference is created.
        obj = null; // To remove strong reference from obj
        System.gc(); // Force to call Garbage collector
        Thread.sleep(5000); // Wait GC finish its job
        System.out.println("End");
    }
}

// 2. Weak reference
public static void main(String args[]) {
    obj = new MyObject();
    WeakReference<MyObject> weakObj = new WeakReference<>(obj)
    obj = null; // obj is now eligible for garbage collection.
    // System.gc(); // if gc run here, obj is completely collected
    obj = weakObj.get(); // try to retrieve back obj
}

public class MainActivity extends Activity {
    @ Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new MyAsyncTask(this).execute();
    }
    private static class MyAsyncTask extends AsyncTask {
        private WeakReference<MainActivity> mainActivity;

        public MyAsyncTask(MainActivity mainActivity) {
            this.mainActivity = new WeakReference<>(mainActivity);
        }
        @ Override
        protected Object doInBackground(Object[] params) {
            return doSomeStuff();
        }
        private Object doSomeStuff() {
            //do something to get result
            return new Object();
        }
        @ Override
        protected void onPostExecute(Object object) {
            super.onPostExecute(object);
            if (mainActivity.get() != null){
                //adapt contents
            }
        }
    }
}

3. SoftReference
SoftReference giống với WeakReference ở điểm nó có thể được thu hồi khi GC cần, tuy nhiên điểm khác biệt là trong khi WeakReference được thu hồi ngay lập tức, còn SoftReference lại phụ thuộc vào GC. Tất cả các SoftReference được cho vào các reference Queue và xóa khi cần. GC đảm bảo rằng tất cả các SoftReference tới các đối tượng softly reachable sẽ được thu hồi trước khi hệ thống văng lỗi OutOfMemory, ưu tiên giữ lại những tham chiếu là recently-created hoặc recently-used.

Trong android, không khuyên khích việc sử dụng SoftReference cho việc tạo cache. Lý do là hệ thống sẽ không có đủ thông tin để xóa hoặc giữ tham chiếu nào và có thể dẫn đến tình trạng heap tăng cao. Việc thiếu thông tin này làm giảm công dụng của SoftReference, GC có thể thu hồi sớm những tham chiếu còn dùng, hoặc hao phí bộ nhớ khi giữ những tham chiếu không cần thiết. Vậy nên sử dụng android.util.LruCache thay cho SoftReference khi tạo cache.

4. PhantomReference
An object is phantom reachable if it is neither strongly nor softly nor weakly reachable and has been finalized and there is a path from the roots to it that contains at least one phantom reference.

Một đối tượng là phantom reachable nếu như nó không phải là strongly, weakly hay softly reachable, đã ở trạng thái finalized (tức lúc GC xác định là không còn tham chiếu nào tới đối tượng này) và có 1 đường đi từ roots tới đối tượng đó chứa ít nhất 1 phantom reference

Khác với các tham chiếu khác, PhantomReference không tự động bị thu hồi bởi GC mà nó sẽ được cho vào ReferenceQueue, một đối tượng phantom reachable sẽ tồn tại đến khi tất cả các tham chiếu bị hủy hoặc bản thân nó trở thành unreachable. Khác với WeakReference and SoftReference, hàm get() trong PhantomReference luôn trả về null

// 3. Trong android, không khuyên khích việc sử dụng SoftReference cho việc tạo cache. Lý do là hệ thống sẽ không có đủ thông tin để xóa hoặc giữ tham chiếu nào và có thể dẫn đến tình trạng heap tăng cao. Việc thiếu thông tin này làm giảm công dụng của SoftReference, GC có thể thu hồi sớm những tham chiếu còn dùng, hoặc hao phí bộ nhớ khi giữ những tham chiếu không cần thiết. Vậy nên sử dụng android.util.LruCache thay cho SoftReference khi tạo cache.
