
angular.module('DeploymentCenterApp', []).controller('DeploymentCenterController',['$scope', 'DeploymentCenterService', function($scope, DeploymentCenterService) {
	
	
//	DeploymentCenterService.getPhysicalServers().then(function (data){
//		$scope.physicalServers = data;
//	});
    	  $scope.uploadFile = function(){      		
      		var myFile = document.getElementById("uploadFile");
      		var reader = new FileReader();
  	      	var arrayBuffer = "";
  	      	
      		reader.onload = function(){
      			arrayBuffer = reader.result;
      			arrayBuffer = new Uint8Array(arrayBuffer);
      			
      	    };


      	    reader.onloadend = function(){
      	    	var string = new TextDecoder("utf-8").decode(arrayBuffer);
      	    	var warModel = {
	      			hostName : "localhost",
	      			serverPort : "9000",
	      			serverUserame : "admin",
	      			serverPassword : "admin",
	      			deployableFile : string,
	      			deployableName : myFile.files[0].name,
	      			serverName : "tomcat8x"
	      		};
	      		DeploymentCenterService.deploy(warModel);
      	    }
      	  reader.readAsArrayBuffer(myFile.files[0]);
      	    
      		
      	}



      	function pack(bytes) {
		    var str = "";
		    for(var i = 0; i < bytes.length; i += 2) {
		        var char = bytes[i] << 8;
		        if (bytes[i + 1])
		            char |= bytes[i + 1];
		        str += String.fromCharCode(String,char);
		    }
		    return str;
		}
}]);


//angular.module('DeploymentCenterApp').directive('fileBrowser', function() {
//    return {
//        restrict: 'A',
//        replace: true,
//        transclude: true,
//        scope: false,
//        template:
//            '<div class="input-prepend extended-date-picker">'+
//                '<input type="button" class="btn" value="browse...">'+
//                '<input type="text" readonly class="override">'+
//                '<div class="proxied-field-wrap" ng-transclude></div>'+
//            '</div>',
//        link: function($scope, $element, $attrs, $controller) {
//            var button, fileField, proxy;
//            fileField = $element.find('[type="file"]').on('change', function() {
//                proxy.val(angular.element(this).val());
//            });
//            proxy = $element.find('[type="text"]').on('click', function() {
//                fileField.trigger('click');
//            });
//            button = $element.find('[type="button"]').on('click', function() {
//                fileField.trigger('click');
//            });
//        }
//    };
//});