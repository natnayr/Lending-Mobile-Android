package com.crowdo.p2pconnect.view.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.data.client.BiddingClient;
import com.crowdo.p2pconnect.helpers.CallBackUtil;
import com.crowdo.p2pconnect.helpers.ConstantVariables;
import com.crowdo.p2pconnect.helpers.HTTPResponseUtils;
import com.crowdo.p2pconnect.helpers.SnackBarUtil;
import com.crowdo.p2pconnect.model.core.Investment;
import com.crowdo.p2pconnect.model.core.Loan;
import com.crowdo.p2pconnect.model.core.UpdateBid;
import com.crowdo.p2pconnect.model.response.BidStatusResponse;
import com.crowdo.p2pconnect.model.response.MessageResponse;
import com.crowdo.p2pconnect.oauth.AuthAccountUtils;
import com.crowdo.p2pconnect.view.activities.CheckoutActivity;
import com.crowdo.p2pconnect.viewholders.ItemCheckoutSummaryViewHolder;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

public class CheckoutSummaryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final String LOG_TAG = CheckoutSummaryAdapter.class.getSimpleName();
    private Context mContext;
    private List<CheckoutListItem> biddingList;
    private List<UpdateListItem> updatingList;
    private Disposable disposablePostDeleteBid;
    private RecyclerView mRecyclerView;
    private TextView mHeaderNoOfLoans;
    private CallBackUtil<Boolean> callBackUtilToFragment;


    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public CheckoutSummaryAdapter(Context context, RecyclerView recyclerView, CallBackUtil<Boolean> callBackUtil) {
        this.mContext = context;
        this.biddingList = new ArrayList<>();
        this.updatingList = new ArrayList<>();
        this.mRecyclerView = recyclerView;
        this.callBackUtilToFragment = callBackUtil;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(viewType == TYPE_HEADER){
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.header_checkout_summary, parent, false);
            return new HeaderCheckoutSummaryViewHolder(view);
        }else if(viewType == TYPE_ITEM){
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_checkout_summary, parent, false);

            ItemCheckoutSummaryViewHolder viewHolder =
                    new ItemCheckoutSummaryViewHolder(view, mContext, this);

            viewHolder.initView();

            return viewHolder;
        }

        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof HeaderCheckoutSummaryViewHolder){
            HeaderCheckoutSummaryViewHolder headerHolder = (HeaderCheckoutSummaryViewHolder) holder;
            Log.d(LOG_TAG, "APP onBindViewHolder Pending Bids Count: " + biddingList.size());

            mHeaderNoOfLoans = headerHolder.mHeaderCheckoutSummaryNoOfLoans;
            mHeaderNoOfLoans.setText(Integer.toString(biddingList.size()));

        }else if(holder instanceof ItemCheckoutSummaryViewHolder){
            final int listPosition = holder.getAdapterPosition()-1;
            final ItemCheckoutSummaryViewHolder itemHolder = (ItemCheckoutSummaryViewHolder) holder;

            Log.d(LOG_TAG, "APP onBindViewHolder listPosition = " + listPosition +
                    ", getLayoutPosition() = " + itemHolder.getLayoutPosition());

            //taking note of header, thus position-1
            final CheckoutListItem bidListItem = biddingList.get(listPosition);

            itemHolder.populateItemDetails(itemHolder.getLayoutPosition(), bidListItem);
        }

    }

    @Override
    public int getItemCount() {
        //investment list should be equal to loan list + header
        if(biddingList.size() == 0 ){
            return 1;
        }
        if(biddingList == null){
            return 0;
        }
        return biddingList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if(isPositionHeader(position)){
            return TYPE_HEADER;
        }else {
            return TYPE_ITEM;
        }
    }

    public void setBiddingInvestmentsAndLoans(@Nullable List<Investment> investments,
                                              @Nullable List<Loan> loans){
        if(investments == null || loans == null){
            return; //must not be null
        }else if(investments.size() != loans.size()){
            return; //must be equal in size
        }
        biddingList.clear();
        Iterator<Investment> investIter = investments.iterator();
        Iterator<Loan> loanIter = loans.iterator();
        while(investIter.hasNext() && loanIter.hasNext()){
            biddingList.add(new CheckoutListItem(investIter.next(), loanIter.next()));
        }

        notifyDataSetChanged();
    }



    private boolean isPositionHeader (int position){
        return position == 0;
    }

    public void addToUpdateList(int toUpdateInvestUnitAmount, Investment investment){
        long toUpdateInvestAmount = toUpdateInvestUnitAmount * ConstantVariables.IDR_BASE_UNIT;
//        if(!toUpdateInvestmentList.contains(investment)){
//            if(investment.getInvestAmount() != toUpdateInvestAmount){
//
//            }
//        }
    }

    public void removeDisposablePostDeleteBid() {
        if(disposablePostDeleteBid != null){
            disposablePostDeleteBid.dispose();
        }
    }

    public void doDelete(final int layoutPosition, final CheckoutListItem pairItem){
        //do check here
        final BiddingClient bidClient = BiddingClient.getInstance(mContext);

        bidClient.postDeleteBid(pairItem.investment.getId(),
                        ConstantVariables.getUniqueAndroidID(mContext))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<BidStatusResponse>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposablePostDeleteBid = d;
                    }

                    @Override
                    public void onNext(@NonNull Response<BidStatusResponse> response) {
                        if(response.isSuccessful()){
                            BidStatusResponse deleteBidResponse = response.body();
                            if(mRecyclerView !=null) {
                                SnackBarUtil.snackBarForInfoCreate(mRecyclerView,
                                        deleteBidResponse.getServer().getMessage(),
                                        Snackbar.LENGTH_SHORT).show();
                            }

                        }else{
                            Log.d(LOG_TAG, "APP getCheckoutSummary onNext() status > " + response.code());
                            if(HTTPResponseUtils.check4xxClientError(response.code())){
                                if(ConstantVariables.HTTP_UNAUTHORISED == response.code()){
                                    AuthAccountUtils.actionLogout((CheckoutActivity) mContext);
                                }else if(ConstantVariables.HTTP_NOT_FOUND == response.code()){
                                    String serverErrorMessage = "Error: Delete Bid Not Successful";
                                    if(response.errorBody() != null) {
                                        Converter<ResponseBody, MessageResponse> errorConverter =
                                                bidClient.getRetrofit().responseBodyConverter(
                                                        MessageResponse.class, new Annotation[0]);
                                        try{
                                            MessageResponse errorResponse = errorConverter
                                                    .convert(response.errorBody());
                                            serverErrorMessage = errorResponse.getServerResponse().getMessage();
                                        }catch (IOException e){
                                            e.printStackTrace();
                                            Log.e(LOG_TAG, "ERROR: " + e.getMessage(), e);
                                        }

                                        SnackBarUtil.snackBarForErrorCreate(mRecyclerView,
                                                serverErrorMessage, Snackbar.LENGTH_SHORT)
                                                .show();
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                        Log.e(LOG_TAG, "ERROR: " + e.getMessage(), e);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(LOG_TAG, "APP postDeleteBid Rx onComplete, remove on adapter pos: "
                                + layoutPosition);
                        biddingList.remove(pairItem);
                        notifyItemRemoved(layoutPosition);
                        notifyItemChanged(0); //ask to redraw header that is bound list.size
                        callBackUtilToFragment.eventCallBack(false); //refresh header
                    }
                });
    }

    public class CheckoutListItem{
        public Investment investment;
        public Loan loan;
        public CheckoutListItem(Investment investment, Loan loan){
            this.investment = investment;
            this.loan = loan;
        }

        @Override
        public boolean equals(Object obj) {
            if(!(obj instanceof CheckoutListItem))
                return false;

            CheckoutListItem other = (CheckoutListItem) obj;
            return (this.investment.getId()==other.investment.getId()) &&
                    (this.loan.getId()==other.loan.getId());
        }

        @Override
        public int hashCode() {
            return investment.hashCode();
        }
    }

    class UpdateListItem{
        public Investment investment;
        public UpdateBid updateBid;
        public UpdateListItem(Investment investment, UpdateBid updateBid){
            this.investment = investment;
            this.updateBid = updateBid;
        }

        @Override
        public boolean equals(Object obj) {
            if(!(obj instanceof UpdateListItem))
                return false;
            return ((UpdateListItem) obj).investment.getId()
                    == investment.getId();
        }
        @Override
        public int hashCode() {
            return investment.hashCode();
        }
    }

    class HeaderCheckoutSummaryViewHolder extends RecyclerView.ViewHolder{
        @Nullable @BindView(R.id.checkout_summary_no_of_loans_value)
        TextView mHeaderCheckoutSummaryNoOfLoans;

        @Nullable @BindView(R.id.checkout_summary_no_of_loans_icon)
        ImageView mSummaryCartNoOfLoansIcon;

        @Nullable @BindView(R.id.checkout_summary_swipe_delete_info_icon)
        ImageView mSummaryCartSwipeToDeleteIcon;

        HeaderCheckoutSummaryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            mSummaryCartNoOfLoansIcon.setImageDrawable(
                    new IconicsDrawable(mContext)
                            .icon(CommunityMaterial.Icon.cmd_cash_multiple)
                            .colorRes(R.color.color_secondary_text)
                            .sizeRes(R.dimen.checkout_summary_cart_icon_size));


            mSummaryCartSwipeToDeleteIcon.setImageDrawable(
                    new IconicsDrawable(mContext)
                            .icon(CommunityMaterial.Icon.cmd_chevron_double_left)
                            .colorRes(R.color.color_secondary_text)
                            .sizeRes(R.dimen.item_checkout_summary_cart_swipe_info_icon_size));
        }
    }

}
