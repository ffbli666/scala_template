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
            var id = this.data._id.$oid;
            if (!id) return;
            var that = this;
            that.process = true;
            that.$http.delete("/api/user/" + id, function (response, status, request) {
                that.process = false;
                that.$parent.list.$remove(this.data)
                that.$parent.find = (that.$parent.list.length > 0) ? true : false;
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