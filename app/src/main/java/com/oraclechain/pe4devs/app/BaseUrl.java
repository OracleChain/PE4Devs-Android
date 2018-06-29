package com.oraclechain.pe4devs.app;

/**
 * Created by pocketEos on 2017/11/23.
 */

public class BaseUrl {
    /**
     * 正式环境服务器地址
     */

    public final static String HTTP_CHAIN_ADDRESS = "https://api.pocketeos.top/api_oc_blockchain-v1.3.0/";
    public final static String HTTP_CHAIN_VOTE_ADDRESS = "https://api.pocketeos.top/voteoraclechain/";



    // 获取EOS账号信息
    public final static String HTTP_eos_get_account = HTTP_CHAIN_ADDRESS + "get_account_asset";
    // 获取链上信息
    public final static String HTTP_eos_get_table = HTTP_CHAIN_ADDRESS + "get_table_rows";
    // 获取区块链状态
    public final static String HTTP_get_chain_info = HTTP_CHAIN_ADDRESS + "get_info";
    // 交易JSON序列化
    public final static String HTTP_get_abi_json_to_bin = HTTP_CHAIN_ADDRESS + "abi_json_to_bin";
    // 获取keys
    public final static String HTTP_get_required_keys = HTTP_CHAIN_ADDRESS + "get_required_keys";
    // 发起交易
    public final static String HTTP_push_transaction = HTTP_CHAIN_ADDRESS + "push_transaction";
    // 获取区块链账号信息
    public final static String HTTP_get_chain_account_info = HTTP_CHAIN_ADDRESS + "get_account";

    // 通过公钥获取账号
    public final static String getHTTP_GetAccounts = HTTP_CHAIN_VOTE_ADDRESS + "GetAccounts";
}
