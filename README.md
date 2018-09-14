<h2 id="1">Overview</h2>

Adaptation tool for PocketEOS dapps developers.

[Download Here](https://github.com/OracleChain/PE4Devs-Android/releases/download/2.0/PE4Devs-Android.apk)


<h2 id="2">How to specific an eos account to test in PE4Devs</h2>

1.set wallet info

2.import an eos account

3.get the eos account list

4.choice one

5.input an EosProxyServer address(http://localhost:8080) and start TEST. 


<h2 id="3">Preparation</h2>

1.Detect User-Agent in your webapp :

Android: "PocketEosAndroid", 

IOS: "PocketEosIos"

2.Android specification:

JsInterface Name(the name used to expose the object in JavaScript): DappJsBridge

3.enter webapp's url, and we'll return the PE language enviroment:

url+"?language=Chinese"

url+"?language=English"



<h2 id="4">Get Wallet Info</h2>

1.wallet info

Use js to declare a function named getWalletWithAccount to transfer the wallet Info

tips: need to expose it under global window.The format of "detail" string is in JSON.


eg:
function getWalletWithAccount(String detail){
}

detail：{"account":"eosio","uid":"46eec3f33e3d86a40c914a591922f420","wallet_name":"haha","image":""}

2.eos account info

Use js to declare a function named getEosAccount to transfer the Account Info

tips: need to expose it under global window.The format of "account" is in String.

eg:
function getEosAccount(String account){
}


<h2 id="5">Make Transactions</h2>
[PIC]()

Support multiple action signatures (multiple transfer transactions can also be performed at the same time)

The contract calls the operation flow: the completion is done on the mobile side, and the data is passed by the mobile end declaration method.

Method: pushActions(String serialNumber, String actionsDetails) 

Remarks: dapp contract call to pass serialNumber (initiate transaction serial number, used to initiate corresponding detection of multiple transactions txid at the same time, this field can be customized to maintain uniqueness), actionsDetails (multiple Action information, passed in json form),

eg：
{
  "actions": [
    {
      "account": "contract_name",
      "name": "action_name",
      "action":authorization": [
        {
          "actor": "eos_account_name",
          "permission": "active"
        }
      ],
      "data": {
        "from": "eos_account_name",
        "receiver": "eos_account_name",
        "stake_net_quantity": "4.0000 EOS",
        "stake_cpu_quantity": "4.0000 EOS",
        "transfer": 1
      }
    },
    {
      "account": "contract_name",
      "name": "action_name",
      "authorization": [
        {
          "actor": "eos_account_name",
          "permission": "active"
        }
      ],
      "data": {
        "from": "eos_account_name",
        "to": "eos_account_name",
        "quantity": "2.0000 EOS",
        "memo": "init"
      }
    }
  ]
}

Call Method Below:
Android: window.DappJsBridge.pushActions(serialNumber，actionsDetails)
IOS: window.webkit.messageHandlers.pushActions.postMessage({serialNumber：'123456',actionsDetails：'TEST'});


<h2 id="6">Require txid</h2>

1.The JS side declares methods to pass data. 

Method name: pushActionResult(String serialNumber, String result) 

Remarks: This method, regardless of the front-end framework, needs to be exposed to the window global. 

Eg:
//Get the transaction txid and the corresponding serial number to help detect the transaction information. 
function pushActionResult(String serialNumber,String result) {
} 


2.Transaction interrupt or exception:

If serialNumber is the serialNumber passed in to js, ​​result is "ERROR:{"code":3050003,"what":"eosio_assert_message assertion failure","name":"eosio_assert_message_exception","details":[]}", this When result is the error message on the chain (all error messages will start with ERROR, you can check if the transaction is wrong by checking ERROR.)

If serialNumber is passed in the serialNumber js end, result is "ERROR: you enter the wrong password, enter ~ Please check again", or result is "ERROR:.. Password is invalid Please check it" illustrates the password is wrong.

If serialNumber is the serialNumber passed in js, result is "ERROR: Cancel", or result is "ERROR: Cancel" user cancels the transaction.

3.The transaction is proceeding normally::

If the serialNumber is the serialNumber passed in the js side, the result is txid (eg: 5cf2841c9f1d610d4aac2b8d586da21e2057f4188fdcc02dc4187f0f2d5b177b), indicating that the transaction is completed.







