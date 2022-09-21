package com.degpeg.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.viewpager2.widget.ViewPager2
import com.degpeg.Controller
import com.degpeg.R
import com.degpeg.b2csdk.DegpegSDKProvider
import com.degpeg.b2csdk.DegpegSDKProvider.PROVIDER_ID
import com.degpeg.b2csdk.DegpegSDKProvider.PUBLISHER_ID
import com.degpeg.b2csdk.UserRole
import com.degpeg.b2csdk.UserRole.PROVIDER
import com.degpeg.common.BaseFragment
import com.degpeg.common.Navigation
import com.degpeg.databinding.FragmentHomeBinding
import com.degpeg.model.SessionCategoryItem
import com.degpeg.model.VideoContentItem
import com.degpeg.network.NetworkURL
import com.degpeg.network.NetworkURL.RES_UNAUTHORISED
import com.degpeg.network.typeCall
import com.degpeg.ui.adapter.*
import com.degpeg.utility.*
import com.degpeg.utility.LocalDataHelper
import com.degpeg.utility.pagertransformer.DefaultTransformer
import com.degpeg.viewmodel.ContentViewModel
import org.json.JSONArray
import org.json.JSONObject
import java.lang.RuntimeException

internal class HomeFragment() : BaseFragment(), View.OnClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ContentViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initUi() {
        setTrendingList()
        setUpcomingList()
        setUpImageSlider()

        viewModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.AndroidViewModelFactory(Controller.instance)
        )[ContentViewModel::class.java]

        if (viewModel.mVideoList.value.isNullOrEmpty()) fetchData()
        viewModel.mTopVideoList.observe(this, this::setUpSlider)
        viewModel.mTrendingVideoList.observe(this, this::setUpTrendingData)
        viewModel.mUpcomingVideoList.observe(this, this::setUpcomingData)

        seUpCategoriesList()
        viewModel.getSessionCategories().observe(this, this::seUpCategories)

        // brand
        binding.rvBrands.adapter = BrandImageAdapter(getDummyBrandImage())
        binding.txtBrandTitle.setColorSpan(
            getString(R.string.brands_we_have),
            getString(R.string.we_have),
            ContextCompat.getColor(requireContext(), R.color.black)
        )

        binding.pullToRefresh.setOnRefreshListener {
            fetchData(false)
        }
    }

    /**
     * Trending video section
     * */
    private var trendingVideoAdapter: TrendingVideosAdapter? = null
    private fun setTrendingList() {
        trendingVideoAdapter = TrendingVideosAdapter(requireContext(), this::onVideoItemClick)
        binding.trending.rvTrendingVideos.adapter = trendingVideoAdapter
        binding.trending.txtTrendingHeader.setColorSpan(
            getString(R.string.trending_videos),
            getString(R.string.videos),
            ContextCompat.getColor(requireContext(), R.color.black)
        )
    }

    private fun setUpTrendingData(mList: ArrayList<VideoContentItem>?) {
        if (trendingVideoAdapter == null) setTrendingList()
        trendingVideoAdapter?.set(mList) {
            if (trendingVideoAdapter?.currentList.isNullOrEmpty()) {
                binding.trending.root.gone()
            } else {
                binding.trending.root.visible()
            }
        }
    }

    /**
     * Upcoming video section
     * */
    private var upcomingVideoAdapter: UpcomingEventAdapter? = null
    private fun setUpcomingList() {
        upcomingVideoAdapter = UpcomingEventAdapter(requireContext(), {})
        binding.upcomingShows.rvUpcomingShows.adapter = upcomingVideoAdapter
        binding.upcomingShows.txtHeader.setColorSpan(
            getString(R.string.upcoming_shows),
            getString(R.string.shows),
            ContextCompat.getColor(requireContext(), R.color.black)
        )
    }

    private fun setUpcomingData(mList: ArrayList<VideoContentItem>?) {
        if (upcomingVideoAdapter == null) setUpcomingList()
        upcomingVideoAdapter?.set(mList) {
            if (upcomingVideoAdapter?.currentList.isNullOrEmpty()) {
                binding.upcomingShows.root.gone()
            } else {
                binding.upcomingShows.root.visible()
            }
        }
    }

    private fun onVideoItemClick(data: VideoContentItem) {
        Navigation.startPlayer(requireActivity(), data)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.trending.txtTrendingHeader -> {
                Navigation.openVideoListingActivity(
                    requireActivity(),
                    getString(R.string.trending_videos),
                    getString(R.string.videos),
                    trendingVideoAdapter?.getCurrent() as ArrayList<VideoContentItem>?
                )
            }
            binding.categories.txtHeader -> {
                findNavController(this).navigate(R.id.action_home_to_category)
            }
            binding.upcomingShows.txtHeader -> {
                findNavController(this).navigate(R.id.action_home_to_calender)
            }
        }
    }

    /**
     * Event slider
     * */
    private val pageSliderHandler: Handler = Handler(Looper.getMainLooper())
    private val pageSliderRunnable = Runnable {
        if (_binding == null) return@Runnable
        var newPosition = binding.viewPagerSlider.currentItem + 1
        if (newPosition >= (binding.viewPagerSlider.adapter?.itemCount ?: 0)) newPosition = 0
        binding.viewPagerSlider.setCurrentItem(newPosition, true)
    }

    private fun resetSliderTime() {
        pageSliderHandler.removeCallbacks(pageSliderRunnable)
        pageSliderHandler.postDelayed(pageSliderRunnable, 5000)
    }

    private var pagerAdapter: PagerFragmentAdapter? = null
    private fun setUpSlider(topVideoList: ArrayList<VideoContentItem>) {
        pagerAdapter =
            PagerFragmentAdapter(childFragmentManager, getPagerList(topVideoList), lifecycle)
        binding.viewPagerSlider.adapter = pagerAdapter
        binding.viewPagerSlider.setPageTransformer(DefaultTransformer())
        binding.sliderIndicator.apply {
            setSliderWidth(resources.getDimension(com.intuit.sdp.R.dimen._20sdp))
            setSliderHeight(resources.getDimension(com.intuit.sdp.R.dimen._3sdp))
            setupWithViewPager(binding.viewPagerSlider)
        }

        binding.viewPagerSlider.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                resetSliderTime()
            }
        })
    }

    private fun getPagerList(topVideoList: ArrayList<VideoContentItem>): ArrayList<BaseFragment> {
        val list = ArrayList<BaseFragment>()
        topVideoList.filter {
            list.add(HomePagerItemFragment(it))
        }
        return list
    }

    /**
     * Manage category section
     * */
    private var categoryAdapter: CategoryAdapter? = null
    private fun seUpCategoriesList() {
        categoryAdapter = CategoryAdapter()
        binding.categories.rvCategory.adapter = categoryAdapter
    }

    private fun seUpCategories(list: ArrayList<SessionCategoryItem>) {
        if (categoryAdapter == null) setUpcomingList()
        categoryAdapter?.set(list) {
            if (categoryAdapter?.currentList.isNullOrEmpty()) {
                binding.categories.root.gone()
            } else {
                binding.categories.root.visible()
            }
        }
    }

    /**
     * image slider
     * */
    private fun setUpImageSlider() {
        binding.rvSliderImage.onFlingListener = null
        PagerSnapHelper().attachToRecyclerView(binding.rvSliderImage)
        binding.rvSliderImage.adapter =
            SliderImageAdapter(getDummyImage(), callback = { _, _ -> })

        binding.recyclerViewIndicator.setRecyclerView(binding.rvSliderImage)
    }

    private fun getDummyImage(): ArrayList<Int> {
        val list = ArrayList<Int>()
        list.add(R.drawable.dummy1)
        list.add(R.drawable.dummy1)
        list.add(R.drawable.dummy1)
        list.add(R.drawable.dummy1)
        return list
    }

    private fun getDummyBrandImage(): ArrayList<Int> {
        val list = ArrayList<Int>()
        list.add(R.drawable.dummy_brand)
        list.add(R.drawable.dummy_brand)
        list.add(R.drawable.dummy_brand)
        return list
    }

    override fun onPause() {
        super.onPause()
        pageSliderHandler.removeCallbacks(pageSliderRunnable)
    }

    override fun onResume() {
        super.onResume()
        resetSliderTime()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    /**
     * Fetch Data
     * */
    private var refreshTokenAttempt = false
    private fun fetchData(isShowProgress: Boolean = true) {
        binding.txtError.gone()
        if (isShowProgress) {
            binding.layData.gone()
            binding.contentLoader.visible()
        }

        if (DegpegSDKProvider.USER_ROLE == PROVIDER) {
            getProvidersSession(arrayListOf(PROVIDER_ID), isShowProgress)
            return
        }

        viewModel.getContentProviders(PUBLISHER_ID).observe(this) { contentResponse ->
            contentResponse.status.typeCall(
                success = {
                    if (contentResponse.data != null) {
                        getProvidersSession(contentResponse.data.contentProviders, isShowProgress)
                    } else {
                        onError(getString(R.string.no_data_found))
                    }
                },
                error = {
                    if (contentResponse.errorCode == RES_UNAUTHORISED && !refreshTokenAttempt)
                        unauthorisedError(isShowProgress)
                    else onError(contentResponse.message)
                }
            )
        }
    }

    private fun getProvidersSession(
        providerIds: ArrayList<String>,
        isShowProgress: Boolean = true
    ) {
        viewModel.getContentProvidersWiseVideos(
            providerIds, getFilter()
        ).observe(this) {
            it.status.typeCall(
                success = { modifyData(it.data) },
                error = {
                    if (it.errorCode == RES_UNAUTHORISED && !refreshTokenAttempt)
                        unauthorisedError(isShowProgress)
                    else onError(it.message)
                }
            )
        }
    }

    private fun unauthorisedError(isShowProgress: Boolean = true) {
        LocalDataHelper.authToken = ""
        refreshTokenAttempt = true
        DegpegSDKProvider.updateAuthToken(
            DegpegSDKProvider.APP_ID,
            DegpegSDKProvider.SECRET_KEY,
            onSuccess = {
                activity?.runOnUiThread {
                    fetchData(isShowProgress)
                }
            },
            onError = {
                onError(getString(R.string.no_data_found))
            })
    }

    private fun modifyData(data: ArrayList<VideoContentItem>?) {
        viewModel.getStreamToWebData(data).observe(this) {
            it.status.typeCall(
                success = {
                    if (it.data != null) {
                        viewModel.updateList(it.data)
                        binding.contentLoader.gone()
                        binding.pullToRefresh.isRefreshing = false
                        if (!binding.layData.isVisible) binding.layData.visible()
                        Log.e("Final Data is : ${it.data}")
                    } else {
                        onError(getString(R.string.no_data_found))
                    }
                },
                error = {
                    onError(it.message)
                }
            )
        }
    }

    private fun getFilter(): HashMap<String, Any> {
        val jsonObject = JSONObject()
        jsonObject.put(
            "include",
            JSONArray().put(JSONObject().put("relation", "liveSessionCategory"))
        )
        jsonObject.put(
            "where",
            JSONObject().put("status", JSONObject().put("neq", "deleted"))
        )
        return hashMapOf(Pair("filter", jsonObject.toString()))
    }

    private fun onError(message: String) {
        binding.contentLoader.gone()
        binding.pullToRefresh.isRefreshing = false
        binding.txtError.visible()
        binding.txtError.text = message
    }

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }
}