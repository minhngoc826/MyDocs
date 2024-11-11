// 1.ActivityFragment.java

// 1. Activity


// 2. Fragment:			https://developer.android.com/guide/fragments/transactions?hl=en
// 2.1. Common
- Setup: AndroidX Fragment
	+ build.gradle: 	implementation "androidx.fragment:fragment:$fragment_version"		// Java language implementation
						implementation "androidx.fragment:fragment-ktx:$fragment_version"	// Kotlin
	+ settings.gradle: 	 Bạn cần thêm kho lưu trữ Google Maven

- Define: ExampleFragment extends Fragment {
	public ExampleFragment() {
        super(R.layout.example_fragment);
    }
 }

- Types:
	+ Fragment:
		- AppCompatActivity extnends FragmentActivity: should use FragmentContainerView (Activity created -> FragmentContainerView: onInflate() -> inflate fragment
	+ DialogFragment: Displays a floating dialog. https://developer.android.com/guide/fragments/dialogs
	+ PreferenceFragmentCompat: Displays a hierarchy of Preference objects as a list. https://developer.android.com/develop/ui/views/components/settings

- Method:
	+ Lifecycle: onAttach -> onCreate -> onCreatedView -> onStart -> onResume -> onStop -> onPause -> onDestroyView -> onDestroy -> onDetach -> Finish.
	+ onAttach: set context
	+ onCreatedView: inflate layout to view -> return view: findViewById, setOnClickListener, ...
	+ get bundleData: requireArguments()
	+ khi add fragment to activity, sau đó activity đc recreate, fragment đc tự động khôi phục từ savedInstanceState.
	+ Fragment can contains other fragment (child fragments)
	
- Back stack: same back stack of activity
	+ addToBackStack: getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_adapter, ListViewFragment.class, "list_view_fragment")
												 .setReorderingAllowed(true).addToBackStack("list_view_fragment").commit();
	+ find fragment: getSupportFragmentManager().findFragmentByTag("list_view_fragment")
	 				 getSupportFragmentManager()..findFragmentById(R.id.fragment_container_adapter)
	+ popBackStack:
	+ ...
	

// 2.2. FragmentManager: getSupportFragmentManager() from activity or fragment
- At runtime, a FragmentManager can add, remove, replace, and perform other actions with fragments in response to user interaction.
- getSupportFragmentManager() / getChildFragmentManager() / getParentFragmentManager():
	+ Activity: contains Fragment -> getSupportFragmentManager()
	+ Fragment: contains childFragments
		- getParentFragmentManager() = getSupportFragmentManager() - activity
		- getChildFragmentManager() = getParentFragmentManager()-childFragments
	+ childFragments:
		- getParentFragmentManager() = getChildFragmentManager() - Fragment
	
- FragmentFactory: Provide dependencies to your fragments
	+ public class MyFragmentFactory extends FragmentFactory {
			private DessertsRepository repository;

- addToBackStack(): STOPPED -> RESUMED khi user back

- setPrimaryNavigationFragment()

- Support multiple back stacks: first, addToBackStack(tag), then
	+ saveBackStack(tag): need setReorderingAllowed(), same popBackStack
	+ restoreBackStack(tag)
	

// 2.3. FragmentTransaction: getSupportFragmentManager().beginTransaction() -> Add operations -> commit()
- setReorderingAllowed(true): Each FragmentTransaction should use setReorderingAllowed(true), tối ưu hoá các thay đổi về trạng thái của các mảnh có liên quan trong giao dịch để các ảnh động và hiệu ứng chuyển tiếp hoạt động chính xác
- add(fragment, bundleData): fragment is RESUMED, nên dùng khi container là FragmentContainerView
- remove(fragment): fragment is DESTROYED.
- replace(fragment, bundleData): = remove + add
- commit():	is asynchronous(không đồng bộ) -> the transaction is scheduled to run on the main UI thread as soon as it is able to do so
	+ all commit can run now by calling: executePendingTransactions() & support addToBackStack
- commitNow(): run the fragment transaction on your UI thread immediately, but not support addToBackStack
- setCustomAnimations():	fragmentManager. beginTransaction()
										   .setCustomAnimations(enter1, exit1)
										   .add(MyFragmentClass, args, tag1) // this fragment gets the first animations
										   .setCustomAnimations(enter2, exit2)
										   .add(MyFragmentClass, args, tag2) // this fragment gets the second animations
										   .commit()
- setMaxLifecycle(): Limit the fragment's lifecycle
- show & hide(fragment): These methods set the visibility of the fragment's views without affecting the lifecycle of the fragment.
- attach() & detach():  not call detach then call attach, should be call: detach -> commit & executePendingOperations() = commitNow -> attach
	+ detach = STOPPED & destroying its view hierarchy & removed from UI, but not DESTROYED, FragmentManager can managed it.
	+ attach = create its view hierarchy & attached & displayUI
	
- Notes: khi add fragment nên dùng class thay vì instance để đảm bảo rằng các cơ chế tạo fragment này cũng được dùng để khôi phục fragment từ một trạng thái đã lưu.


/////////////////////
//pick an easily remembered tag
public void replace(Fragment fragment, String tag){
	FragmentManager man = dashboard.support;
	FragmentTransaction fragt = man.beginTransaction();

	if(!fragment.isAdded()) {
		dashboard.lastTag = dashboard.fragtag;//not needed, but helpful w/ backpresses
		fragt.add(R.id.fragment_container, fragment, tag)
				.hide(man.findFragmentByTag(fragtag)).commit();
		dashboard.fragtag = dashboard.tag;//not needed, but helpful w/ backpresses
	}
	if(fragment.isAdded() && fragment.isHidden()) {
		dashboard.lastTag = dashboard.fragtag;//not needed, but helpful w/ backpresses
		fragt.show(fragment);
		fragt.hide(man.findFragmentByTag(fragtag)).commit();
		dashboard.fragtag = dashboard.tag;//not needed, but helpful w/ backpresses
	}
}

@Override
public void onBackPressed() {
    FragmentManager man = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = man.beginTransaction();
    fragmentTransaction.hide(getSupportFragmentManager().findFragmentByTag(fragtag))
    .show(getSupportFragmentManager().findFragmentByTag(lastTag)).commit();
    fragtag = lastTag;// holds the last fragment
}
