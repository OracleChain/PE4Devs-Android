package com.oraclechain.pe4devs.bean;

import com.google.gson.JsonElement;

import java.util.List;

/**
 * Created by pocketEos on 2018/8/14.
 */

public class PushActionsBean {


    /**
     * type : actions_QRCode
     * actions : [{"account":"octtothemoon","name":"transfer","authorization":[{"actor":"oraclechain4","permission":"active"}],"data":{"from":"oraclechain4","to":"lucan1gogogo","quantity":"1.0000 OCT","memo":"init"}},{"account":"octtothemoon","name":"transfer","authorization":[{"actor":"oraclechain4","permission":"active"}],"data":{"from":"oraclechain4","to":"hellojinpeng","quantity":"1.0000 OCT","memo":"init"}}]
     */

    private String type;
    private List<ActionsBean> actions;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<ActionsBean> getActions() {
        return actions;
    }

    public void setActions(List<ActionsBean> actions) {
        this.actions = actions;
    }

    public static class ActionsBean {
        /**
         * account : octtothemoon
         * name : transfer
         * authorization : [{"actor":"oraclechain4","permission":"active"}]
         * data : {"from":"oraclechain4","to":"lucan1gogogo","quantity":"1.0000 OCT","memo":"init"}
         */

        private String account;
        private String name;
        private JsonElement data;

        public JsonElement getData() {
            return data;
        }

        public void setData(JsonElement data) {
            this.data = data;
        }

        private List<AuthorizationBean> authorization;

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }



        public List<AuthorizationBean> getAuthorization() {
            return authorization;
        }

        public void setAuthorization(List<AuthorizationBean> authorization) {
            this.authorization = authorization;
        }


        public static class AuthorizationBean {
            /**
             * actor : oraclechain4
             * permission : active
             */

            private String actor;
            private String permission;

            public String getActor() {
                return actor;
            }

            public void setActor(String actor) {
                this.actor = actor;
            }

            public String getPermission() {
                return permission;
            }

            public void setPermission(String permission) {
                this.permission = permission;
            }
        }
    }
}
