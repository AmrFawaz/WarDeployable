(function (angular) {
    "use strict";
    function DeploymentCenterService(http, q) {

        function getDeploymentServices(warModel){
        	
            var deferred = q.defer();
            http.get('getDeploymentServices').success(function (data) {
        		deferred.resolve(data);
            }).error(function (error) {
                deferred.resolve(error);
            });
            return deferred.promise;
        }

        function getPhysicalServers(){
            
            var deferred = q.defer();
            http.get('getPhysicalServers').success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.resolve(error);
            });
            return deferred.promise;
        }

        return {
        	getDeploymentServices:getDeploymentServices,
            getPhysicalServers:getPhysicalServers
        };

    }
    DeploymentCenterService.$inject = ['$http', '$q'];
    angular.module('DeploymentCenterApp').factory('DeploymentCenterService', DeploymentCenterService);
}(window.angular));