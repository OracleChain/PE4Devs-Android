package com.oraclechain.pe4devs.view.convenientbanner.adapter;

/**
 * Created by pocketEos on 2018/1/3.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.oraclechain.pe4devs.R;
import com.oraclechain.pe4devs.blockchain.chain.Action;
import com.oraclechain.pe4devs.util.JsonUtil;
import com.oraclechain.pe4devs.view.convenientbanner.holder.Holder;


/**
 * 设置加载图片类型
 */
public class ActionHolderView implements Holder<Action> {
    private View view;

    @Override
    public View createView(Context context) {
        view = LayoutInflater.from(context).inflate(R.layout.item_dapp_show_action, null, false);
        return view;
    }

    @Override
    public void UpdateUI(Context context, int i, Action item) {
        TextView contract_name =  (TextView) view.findViewById(R.id.contract_name);
        TextView action =  (TextView) view.findViewById(R.id.action);
        TextView action_account =  (TextView) view.findViewById(R.id.action_account);
        TextView action_data =  (TextView) view.findViewById(R.id.action_data);

        contract_name.setText(item.getAccount());
        action.setText(item.getName());
        action_account.setText(item.getAuthorization().get(0).getAccount());
        action_data.setText(" " + JsonUtil.stringToJSON(new Gson().toJson(item.getData())).replace("{", "").replace("}", "").trim());
    }
}

