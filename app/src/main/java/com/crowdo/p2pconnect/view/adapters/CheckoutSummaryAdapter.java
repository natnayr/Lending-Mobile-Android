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
import com.crowdo.p2pconnect.model.request.InvestBid;
import com.crowdo.p2pconnect.model.response.BidOnlyResponse;
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
    private List<Investment> biddingInvestmentList;
    private List<Loan> biddingLoanList;
    private List<UpdateItem> updatingList;
    private Disposable disposablePostDeleteBid;
    private RecyclerView mRecyclerView;
    private TextView mHeaderNoOfLoans;
    private CallBackUtil<Boolean> callBackPopulateSummaryList;
    private CallBackUtil<Boolean> callBackShowUpdateBtn;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public CheckoutSummaryAdapter(Context context, RecyclerView recyclerView,
                                  CallBackUtil<Boolean> callBackPopulateSummaryList,
                                  CallBackUtil<Boolean> callBackShowUpdateBtn) {
        this.mContext = context;
        this.biddingInvestmentList = new ArrayList<>();
        this.biddingLoanList = new ArrayList<>();
        this.updatingList = new ArrayList<>();
        this.mRecyclerView = recyclerView;
        this.callBackPopulateSummaryList = callBackPopulateSummaryList;
        this.callBackShowUpdateBtn =  callBackShowUpdateBtn;
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
            Log.d(LOG_TAG, "APP onBindViewHolder Pending Bids Count: " + biddingInvestmentList.size());

            mHeaderNoOfLoans = headerHolder.mHeaderCheckoutSummaryNoOfLoans;
            mHeaderNoOfLoans.setText(Integer.toString(biddingInvestmentList.size()));

        }else if(holder instanceof ItemCheckoutSummaryViewHolder){
            final int listPosition = holder.getAdapterPosition()-1;
            final ItemCheckoutSummaryViewHolder itemHolder = (ItemCheckoutSummaryViewHolder) holder;

            //taking note of header, thus position-1
            final Investment bidInvestmentItem = biddingInvestmentList.get(listPosition);
            final Loan bidLoanItem = biddingLoanList.get(listPosition);

            itemHolder.populateItemDetails(itemHolder.getLayoutPosition(), bidInvestmentItem,
                    bidLoanItem);
        }
    }

    @Override
    public int getItemCount() {
        //investment list should be equal to loan list + header
        if(biddingInvestmentList.size() == 0 ){
            return 1;
        }
        if(biddingInvestmentList == null){
            return 0;
        }
        return biddingInvestmentList.size() + 1;
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
        biddingInvestmentList.clear();
        biddingLoanList.clear();
        updatingList.clear(); //clear updating list
        callBackShowUpdateBtn.eventCallBack(false);
        biddingInvestmentList.addAll(investments);
        biddingLoanList.addAll(loans);

        notifyDataSetChanged();
    }

    private boolean isPositionHeader (int position){
        return position == 0;
    }

    public void addToUpdateList(int toUpdateInvestUnit, Investment investment){
        long toUpdateInvestAmount = toUpdateInvestUnit * ConstantVariables.IDR_BASE_UNIT;
        UpdateItem updateItem = new UpdateItem(investment,
                new InvestBid(investment.getId(), toUpdateInvestAmount));

        if(!updatingList.contains(updateItem)){
            updatingList.add(updateItem);
        }else{
            updatingList.remove(updateItem);
            if(updateItem.investment.getInvestAmount() != toUpdateInvestAmount
                    && toUpdateInvestAmount >= 0) {
                updatingList.add(updateItem);
            }
        }

        callBackShowUpdateBtn.eventCallBack(updatingList.size() > 0);
    }

    public void removeDisposablePostDeleteBid() {
        if(disposablePostDeleteBid != null){
            disposablePostDeleteBid.dispose();
        }
    }

    public List<InvestBid> getUpdateBidList() {
        List<InvestBid> investBidList = new ArrayList<>();

        Iterator<UpdateItem> uiit = updatingList.iterator();
        while(uiit.hasNext()){
            investBidList.add(uiit.next().investBid);
        }
        return investBidList;
    }

    public List<InvestBid> getInvestmentBidList(){
        //enforce that all updates are made and biddingList is not empty
        if(!updatingList.isEmpty() && biddingInvestmentList.isEmpty()){
            return null;
        }

        List<InvestBid> investBidList = new ArrayList<>();
        Iterator<Investment> iit = biddingInvestmentList.iterator();
        while(iit.hasNext()){
            Investment investment = iit.next();
            investBidList.add(new InvestBid(investment.getId(), investment.getInvestAmount()));
        }
        return investBidList;
    }

    public void doDelete(final int layoutPosition, final Investment bidInvestmentItem,
                         final Loan bidLoanItem){
        //do check here
        final BiddingClient bidClient = BiddingClient.getInstance(mContext);

        bidClient.postDeleteBid(bidInvestmentItem.getId(),
                        ConstantVariables.getUniqueAndroidID(mContext))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<BidOnlyResponse>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposablePostDeleteBid = d;
                    }

                    @Override
                    public void onNext(@NonNull Response<BidOnlyResponse> response) {
                        if(response.isSuccessful()){
                            BidOnlyResponse deleteBidResponse = response.body();
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

                                        //hard refresh
                                        callBackPopulateSummaryList.eventCallBack(true);
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
                        biddingInvestmentList.remove(bidInvestmentItem);
                        biddingLoanList.remove(bidLoanItem);
                        notifyItemRemoved(layoutPosition);
                        notifyItemChanged(0); //ask to redraw header that is bound list.size
                        callBackPopulateSummaryList.eventCallBack(false); //refresh header
                    }
                });
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

    public class UpdateItem {
        public Investment investment;
        public InvestBid investBid;
        public UpdateItem(Investment investment, InvestBid investBid){
            this.investment = investment;
            this.investBid = investBid;
        }

        @Override
        public boolean equals(Object obj) {
            if(!(obj instanceof UpdateItem))
                return false;
            UpdateItem other = (UpdateItem) obj;
            return (other.investment.getId() == this.investment.getId());
        }

        @Override
        public int hashCode() {
            return investment.hashCode();
        }
    }

}
