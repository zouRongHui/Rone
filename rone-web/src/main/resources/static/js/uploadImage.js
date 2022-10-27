/**
 * H5页面图片上传组件
 * 依赖于
 *  <link rel="stylesheet" href="https://cdn.bootcss.com/weui/1.1.3/style/weui.min.css">
 *  <link rel="stylesheet" href="https://cdn.bootcss.com/jquery-weui/1.2.1/css/jquery-weui.min.css">
 *  <script src="https://cdn.bootcss.com/jquery/1.11.0/jquery.min.js"></script>
 *  <script src="https://cdn.bootcss.com/jquery-weui/1.2.1/js/jquery-weui.min.js"></script>
 *  <script src="/js/lrz-4.9.35/lrz.all.bundle.js"></script>
 *  Demo:
 *      uploadImageInit("uploaderInput", "uploaderFiles");
 *      // 图片数据在 uploadImageObject.imgFiles 数组中
 * @author rone
 * @date 2021-04-08 9:45
 */

var uploadImageObject = new Object();

/**
 * 图片上传组件
 * @param uploadInput   选择图片组件
 * @param imagesElement 图片展示区域
 * @returns {Object}
 */
function uploadImageInit(uploadInput, imagesElement) {
    // 存放图片路径
    uploadImageObject.imgSrcs = [];
    // 存放文件流
    uploadImageObject.imgFiles = [];
    // 存放图片名字
    uploadImageObject.imgNames = [];
    // 图片进行压缩的临界值 单位KB
    var compressedSize = 500;
    // 压缩后的图片宽度
    var compressedWidth = 1000;

    // 添加上传的图片
    $("#" + uploadInput).on("change", function () {
        // 获取新操作上传的图片
        var fileList = this.files;
        for (var i = 0; i < fileList.length; i++) {
            var imgSrc = getFileSrc(fileList[i]);
            uploadImageObject.imgSrcs.push(imgSrc);
            var fileName = fileList[i].name;
            uploadImageObject.imgNames.push(fileName);
            // 如果图片过大则进行压缩
            if (Math.floor(fileList[i].size / 1024) > compressedSize) {
                lrz(fileList[i], {
                    width: compressedWidth,
                    quality: 1
                }).then(function (rst) {
                    // 此处无法直接用 fileList[i].name 获取到文件名称
                    uploadImageObject.imgFiles.push(datatoFile(rst.base64, fileName));
                }).catch(function (err) {
                    $.toast(error, err);
                }).always(function () {
                    // 不管成功失败都会执行
                });
            } else {
                uploadImageObject.imgFiles.push(fileList[i]);
            }

            $("#" + imagesElement).prepend('<li class="weui-uploader__file" onclick="removeImg(this,\'' + imgSrc + '\')" style="background-image: url(' + imgSrc + ')"></li>');
        }
    });
}

/**
 * 删除已添加的图片
 */
function removeImg(obj, imgSrc) {
    $.confirm("确认移除吗？", function () {
        //点击确认后的回调函数
        var index = -1;
        for (var i = 0; i < uploadImageObject.imgSrcs.length; i++) {
            if (imgSrc == uploadImageObject.imgSrcs[i]) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            $.alert("图片移除失败，请重试！");
            return;
        }
        uploadImageObject.imgSrcs.splice(index, 1);
        uploadImageObject.imgFiles.splice(index, 1);
        uploadImageObject.imgNames.splice(index, 1);
        $(obj).remove();
    }, function () {
        //点击取消后的回调函数
    });
}

/**
 * 获取图片预览路径
 */
function getFileSrc(file) {
    var url = null;
    if (window.createObjectURL != undefined) { // basic
        url = window.createObjectURL(file);
    } else if (window.URL != undefined) { // mozilla(firefox)
        url = window.URL.createObjectURL(file);
    } else if (window.webkitURL != undefined) { // webkit or chrome
        url = window.webkitURL.createObjectURL(file);
    }
    return url;
}

/**
 * 将文件流base64字符串转成文件对象
 * @param data
 * @param fileName
 * @returns {File}
 */
function datatoFile(data, fileName) {
    var arr = data.split(','), mine = arr[0].match(/:(.*?);/)[1], bstr = atob(arr[1]), n = bstr.length,
        u8arr = new Uint8Array(n);
    while (n--) {
        u8arr[n] = bstr.charCodeAt(n);
    }
    return new File([u8arr], fileName, {type: mine});
}