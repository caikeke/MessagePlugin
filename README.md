# MessagePlugin
获取短信记录
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
