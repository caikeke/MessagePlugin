# MessagePlugin
获取短信记录
添加ionic android 权限判断和获取插件：
$ ionic cordova plugin add cordova-plugin-android-permissions
$ npm install --save @ionic-native/android-permissions

 this.androidPermissions.checkPermission(this.androidPermissions.PERMISSION.READ_SMS).then(
      success => {
        if (success.hasPermission) {
          cordova.plugins.MessagePlugin.getMessageInfo("",(msg)=>{
            alert(JSON.stringify(msg));
          },null);
        } else {
          this.androidPermissions.requestPermission(this.androidPermissions.PERMISSION.READ_SMS).then(
            success => {
              if (success.hasPermission) {
                cordova.plugins.MessagePlugin.getMessageInfo("",(msg)=>{
                  alert(JSON.stringify(msg));
                },null);
              }
            }
          );
        }
      }, null);
