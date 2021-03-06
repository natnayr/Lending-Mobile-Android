package com.crowdo.p2pconnect.data;

import com.andretietz.retroauth.Authenticated;
import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.model.request.CheckoutBatchRequest;
import com.crowdo.p2pconnect.model.request.DeleteBidRequest;
import com.crowdo.p2pconnect.model.request.NewBidRequest;
import com.crowdo.p2pconnect.model.request.LoginRequest;
import com.crowdo.p2pconnect.model.request.RegisterRequest;
import com.crowdo.p2pconnect.model.request.SocialRequest;
import com.crowdo.p2pconnect.model.request.TopUpSubmitRequest;
import com.crowdo.p2pconnect.model.request.WithdrawSubmitRequest;
import com.crowdo.p2pconnect.model.response.BidOnlyResponse;
import com.crowdo.p2pconnect.model.response.AuthResponse;
import com.crowdo.p2pconnect.model.response.CheckBidResponse;
import com.crowdo.p2pconnect.model.response.CheckoutSummaryResponse;
import com.crowdo.p2pconnect.model.response.CheckoutUpdateResponse;
import com.crowdo.p2pconnect.model.response.DefinitionBankInfoResponse;
import com.crowdo.p2pconnect.model.response.LinkedInAuthUrlResponse;
import com.crowdo.p2pconnect.model.response.LoanDetailResponse;
import com.crowdo.p2pconnect.model.response.LoanListResponse;
import com.crowdo.p2pconnect.model.response.MemberInfoResponse;
import com.crowdo.p2pconnect.model.response.MessageResponse;
import com.crowdo.p2pconnect.model.response.TopUpHistoryResponse;
import com.crowdo.p2pconnect.model.response.TopUpSubmitResponse;
import com.crowdo.p2pconnect.model.response.WithdrawHistoryResponse;
import com.crowdo.p2pconnect.model.response.WithdrawSubmitResponse;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by cwdsg05 on 9/11/16.
 */

public interface APIServices {

    public static final String FACTSHEET_STAGE = "download_factsheet/";
    public static final String FACTSHEET_LANGUAGE_PARAM = "lang=";

    public static final String P2P_BASE_URL = "https://crowdo.co.id/";

    public static final String API_LIVE_BASE_URL = "https://api.crowdo.com/";
    public static final String LIVE_STAGE = "api/v2/";
    public static final String LIVE_DOCS = "docs";

    @POST("oauth/login")
    @Headers({"Content-type: application/json"})
    Observable<Response<AuthResponse>> postLoginUser(@Body LoginRequest data);

    @POST("oauth/register")
    @Headers({"Content-type: application/json"})
    Observable<Response<AuthResponse>> postRegisterUser(@Body RegisterRequest data);

    @GET("oauth/linkedin/request_oauth_url")
    @Headers({"Content-type: application/json"})
    Observable<Response<LinkedInAuthUrlResponse>> getRequestOauthUrl(@Query("device_id") String deviceId);

    @POST("oauth/fb/social_login")
    @Headers({"Content-type: application/json"})
    Observable<Response<AuthResponse>> postFBSocialAuthUser(@Body SocialRequest data);

    @Authenticated({R.string.authentication_ACCOUNT, R.string.authentication_TOKEN})
    @GET("member/info")
    @Headers({"Content-type: application/json"})
    Observable<Response<MemberInfoResponse>> getMemberInfo(@Query("device_id") String deviceId, @Query("site_config") String siteConfig);

    @Authenticated({R.string.authentication_ACCOUNT, R.string.authentication_TOKEN})    @GET("loans/loan_listing")
    @Headers({"Content-type: application/json"})
    Observable<Response<LoanListResponse>> getLoansList(@Query("device_id") String deviceId, @Query("site_config") String siteConfig);

    @Authenticated({R.string.authentication_ACCOUNT, R.string.authentication_TOKEN})
    @GET("loans/loan_details/{id}")
    @Headers({"Content-type: application/json"})
    Observable<Response<LoanDetailResponse>> getLoanDetail(@Path("id") int id, @Query("device_id") String deviceId, @Query("site_config") String siteConfig);

    @Authenticated({R.string.authentication_ACCOUNT, R.string.authentication_TOKEN})
    @POST("bid/check_bid")
    @Headers({"Content-type: application/json"})
    Observable<Response<CheckBidResponse>> postCheckBid(@Body NewBidRequest data);

    @Authenticated({R.string.authentication_ACCOUNT, R.string.authentication_TOKEN})
    @POST("bid/accept_bid")
    @Headers({"Content-type: application/json"})
    Observable<Response<BidOnlyResponse>> postAcceptBid(@Body NewBidRequest data);

    @Authenticated({R.string.authentication_ACCOUNT, R.string.authentication_TOKEN})
    @POST("bid/delete_bid")
    @Headers({"Content-type: application/json"})
    Observable<Response<BidOnlyResponse>> postDeleteBid(@Body DeleteBidRequest data);

    @Authenticated({R.string.authentication_ACCOUNT, R.string.authentication_TOKEN})
    @GET("invest/checkout/summary")
    @Headers({"Content-type: application/json"})
    Observable<Response<CheckoutSummaryResponse>> getCheckoutSummary(@Query("device_id") String deviceId, @Query("site_config") String siteConfig);

    @Authenticated({R.string.authentication_ACCOUNT, R.string.authentication_TOKEN})
    @POST("bid/update_bid_batch")
    @Headers({"Content-type: application/json"})
    Observable<Response<CheckoutUpdateResponse>> postCheckoutUpdate(@Body CheckoutBatchRequest data);

    @Authenticated({R.string.authentication_ACCOUNT, R.string.authentication_TOKEN})
    @POST("invest/checkout/confirm_bids")
    @Headers({"Content-type: application/json"})
    Observable<Response<MessageResponse>> postCheckoutConfirm(@Body CheckoutBatchRequest data);

    @Authenticated({R.string.authentication_ACCOUNT, R.string.authentication_TOKEN})
    @POST("invest/wallet/request_top_up")
    @Headers({"Content-type: application/json"})
    Observable<Response<TopUpSubmitResponse>> postRequestTopUpInit(@Body TopUpSubmitRequest data);

    @Authenticated({R.string.authentication_ACCOUNT, R.string.authentication_TOKEN})
    @Multipart
    @PUT("invest/wallet/upload_top_up_proof")
    Observable<Response<TopUpSubmitResponse>> putRequestTopUpUpload(@Query("top_up_id") long topUpId, @Query("device_id") String deviceId, @Part MultipartBody.Part filePartBody);

    @Authenticated({R.string.authentication_ACCOUNT, R.string.authentication_TOKEN})
    @GET("invest/wallet/top_up_history")
    @Headers({"Content-type: application/json"})
    Observable<Response<TopUpHistoryResponse>> getTopUpHistory(@Query("device_id") String deviceId, @Query("site_config") String siteConfig, @Query("currency") String currency);

    @Authenticated({R.string.authentication_ACCOUNT, R.string.authentication_TOKEN})
    @POST("invest/wallet/request_withdraw")
    @Headers({"Content-type: application/json"})
    Observable<Response<WithdrawSubmitResponse>> postRequestWithdraw(@Body WithdrawSubmitRequest data);

    @Authenticated({R.string.authentication_ACCOUNT, R.string.authentication_TOKEN})
    @GET("invest/wallet/withdrawal_history")
    @Headers({"Content-type: application/json"})
    Observable<Response<WithdrawHistoryResponse>> getWithdrawHistory(@Query("device_id") String deviceId, @Query("site_config") String siteConfig, @Query("currency") String currency);

    @Authenticated({R.string.authentication_ACCOUNT, R.string.authentication_TOKEN})
    @GET("definitions/bank_info/{region}")
    @Headers({"Content-type: application/json"})
    Observable<Response<DefinitionBankInfoResponse>> getDefinitionsBankInfo(@Path("region") String region, @Query("device_id") String deviceId);

}
