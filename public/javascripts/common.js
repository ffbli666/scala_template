//vue component
var DelModal = Vue.extend({
    template:   '<div id="delModal" class="confirm-delete modal fade" tabindex="-1">'
                    +'<div class="modal-center">'
                        +'<div class="modal-dialog">'
                            +'<div class="modal-content">'
                                +'<div class="modal-header">'
                                    +'<button type="button" class="close" data-dismiss="modal"><span>&times;</span></button>'
                                    +'<h4 class="modal-title">'
                                        +'刪除確認'
                                    +'</h4>'
                                +'</div>'
                                +'<div class="modal-body">'
                                    +'請問是否刪除此筆資料?'
                                    +'<div class="text-danger">{{delname}}</div>'
                                +'</div>'
                                +'<div class="modal-footer">'
                                    +'<button type="button" class="btn btn-danger"v-on:click="delData()" v-bind:disabled="process">刪除</button>'
                                    +'<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>'
                                +'</div>'
                            +'</div>'
                        +'</div>'
                    +'</div>'
                +'</div>',
    props: ['data'],
    ready: function() {
        this.delModal = $('#delModal');
    },
    data: function () {
        return {
            process: false,
        };
    },
    methods: {
        delData: function() {
            var id = this.data.id;
            if (!id) return;
            var that = this;
            that.process = true;
            that.$http.delete("/api/user/" + id, function (response, status, request) {
                that.process = false;
                that.$parent.delUser(that.data);
                that.delModal.modal('hide');
            }).error(function (response, status, request) {
                that.process = false;
                console.log(response);
                that.delModal.modal('hide');
            });
        }
    },
    computed: {
        delname: {
            get: function () {
                return this.data.firstname + ' ' + this.data.lastname + ' (' + this.data.email +')';
            },
        }
    }
});
Vue.component('del-modal', DelModal);



//vue component
var EditModal = Vue.extend({
    template:   '<div id="editModal" class="modal fade" tabindex="-1">'
                    +'<div class="modal-center">'
                        +'<div class="modal-dialog modal-lg">'
                            +'<div class="modal-content">'
                                +'<div class="modal-header">'
                                    +'<button type="button" class="close" data-dismiss="modal"><span>&times;</span></button>'
                                    +'<h4 class="modal-title">'
                                        +'<span v-show="create">新增使用者</span>'
                                        +'<span v-show="!create">編輯使用者資料</span>'
                                    +'</h4>'
                                +'</div>'
                                +'<div class="modal-body">'
                                    +'<validator name="validation1">'
                                        +'<form class="form-horizontal" onsubmit="return false;" novalidate>'
                                            +'<input type="hidden" value="{{id}}">'
                                            +'<div class="form-group">'
                                                +'<label class="col-sm-2 control-label"><span class="red">*</span> 名字</label>'
                                                +'<div class="col-sm-10">'
                                                    +'<input type="text" class="form-control" id="firstname" name="firstname" placeholder="例如：小明" v-model="firstname" v-validate:name="[\'required\']">'
                                                +'</div>'
                                            +'</div>'
                                            +'<div class="form-group">'
                                                +'<label class="col-sm-2 control-label"><span class="red">*</span> 姓氏</label>'
                                                +'<div class="col-sm-10">'
                                                    +'<input type="text" class="form-control" id="lastname" name="lastname" placeholder="例如：王" v-model="lastname" v-validate:lastname="[\'required\']">'
                                                +'</div>'
                                            +'</div>'
                                            +'<div class="form-group email-group">'
                                                +'<label class="col-sm-2 control-label"><span class="red">*</span> Email</label>'
                                                +'<div class="col-sm-10">'
                                                    +'<input type="email" class="form-control" id="email" name="email" placeholder="例如：useremail@gmail.com" v-model="email" v-validate:email="[\'required\']" v-bind:disabled="!create" v-on:blur="checkEmail()">'
                                                +'</div>'
                                            +'</div>'
                                            +'<div class="form-group">'
                                                +'<label class="col-sm-2 control-label">性別</label>'
                                                +'<div class="col-sm-10">'
                                                    +'<select class="form-control" id="gender" name="gender" v-model="gender" v-validate:gender="[\'required\']">'
                                                        +'<option value="男">男</option>'
                                                        +'<option value="女">女</option>'
                                                        +'<option value="隱藏">隱藏</option>'
                                                    +'</select>'
                                                +'</div>'
                                            +'</div>'
                                        +'</form>'
                                    +'</validator>'
                                +'</div>'
                                +'<div class="modal-footer">'
                                    +'<span class="red" v-show="error">{{error}}</span> '
                                    +'<button type="button" class="btn btn-primary" v-show="create" v-on:click="createData()" v-bind:disabled="$validation1.invalid || this.processing">新增</button>'
                                    +'<button type="button" class="btn btn-primary" v-show="!create" v-on:click="editData()" v-bind:disabled="$validation1.invalid || this.processing">更新</button>'
                                    +'<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>'
                                +'</div>'
                            +'</div>'
                        +'</div>'
                    +'</div>'
                +'</div>',
    props: ['create', 'id', 'firstname', 'lastname', 'email', 'gender'],
    ready: function() {
        this.editModal = $('#editModal');
    },
    data: function () {
        return {
            process: false,
            error: "",
        };
    },
    methods: {
        init : function() {
            $(".has-error").removeClass("has-error");
            this.error = "";
            this.process = false;
        },
        checkEmail: function() {
            if(!validator.isEmail(this.email)) {
                $(".email-group").addClass("has-error");
                this.error = "Email 格式不正確";
                return false;
            }
            $(".email-group").removeClass("has-error");
            this.error = "";
            return true
        },
        createData : function() {
            var that = this;
            if (!that.checkEmail()) return;
            that.process = true;
            var data = {
                firstname : that.firstname,
                lastname  : that.lastname,
                gender    : that.gender,
                email     : that.email,
            };
            that.$http.post("/api/user", data ,function (response, status, request) {
                that.process = false;
                that.error = "";
                that.firstname = "";
                that.lastname = "";
                that.gender = "";
                that.email = "";
                that.$parent.insertUser(response.result);
                that.editModal.modal('hide');
            }).error(function (response, status, request) {
                that.process = false;
                console.log(response);
                that.error = response.msg;
                //that.editModal.modal('hide');
            });
        },
        editData: function() {
            var that = this;
            if (!that.id) return;
            that.process = true;
            var data = {
                firstname : that.firstname,
                lastname  : that.lastname,
                gender    : that.gender
            };
            that.$http.put("/api/user/" + that.id, data ,function (response, status, request) {
                that.process = false;
                that.$parent.updateUser(response.result);
                that.editModal.modal('hide');
            }).error(function (response, status, request) {
                that.process = false;
                console.log(response);
                that.editModal.modal('hide');
            });
        }
    }
});
Vue.component('edit-modal', EditModal);