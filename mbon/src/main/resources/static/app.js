var app = angular.module("app", ["ngRoute"]);

app.config(function($routeProvider) {

	$routeProvider
	.when("/map", {
		templateUrl : "map.html",
		controller  : "mapcontroller"
	})
	.when("/graph", {
		templateUrl : "graph.html",
		controller  : "graphcontroller"
	})
	.when("/output", {
		templateUrl : "output.html",
		controller  : "outputcontroller"
	})
	.otherwise({
		templateUrl : "home.html",
		controller  : "mboncontroller"
	});

});

app.controller("mboncontroller", function($scope, $http) {

    $scope.network = {};
    $scope.eov = {
        questions: [],
        requirements: []
    };
    $scope.question = {};
    $scope.requirement = {};
    $scope.tool = {};
    $scope.dataproduct = {};
    $scope.datasystem = {};
    $scope.activity = {
        tools: [],
        dataproducts: [],
        datasystems: []
    };
    $scope.temp = {};

    $scope.savenetwork = function() {
        $http.post("network", $scope.network).then(function() {
            updatenetworks();
        });
    };

    $scope.savequestion = function() {
        $http.post("question", $scope.question).then(function() {
            updatequestions();
        });
    };

    $scope.saverequirement = function() {
        $http.post("requirement", $scope.requirement).then(function() {
            updaterequirements();
        });
    };

    $scope.savetool = function() {
        $http.post("tool", $scope.tool).then(function() {
            updatetools();
        });
    };

    $scope.savedataproduct = function() {
        $http.post("dataproduct", $scope.dataproduct).then(function() {
            updatedataproducts();
        });
    };

    $scope.savedatasystem = function() {
        $http.post("datasystem", $scope.datasystem).then(function() {
            updatedatasystems();
        });
    };

    $scope.saveeov = function() {
        $http.post("eov", $scope.eov).then(function() {
            updateeovs();
        });
    };

    $scope.saveactivity = function() {
        $http.post("activity", $scope.activity).then(function() {
            updateactivities();
        });
    };

    $scope.deletenetwork = function(id) {
        $http.delete("network/" + id).then(function() {
            updatenetworks();
        });
    };

    $scope.deleteactivity = function(id) {
        $http.delete("activity/" + id).then(function() {
            updateactivities();
        });
    };

    $scope.deleteeov = function(id) {
        $http.delete("eov/" + id).then(function() {
            updateeovs();
        });
    };

    $scope.deletequestion = function(id) {
        $http.delete("question/" + id).then(function() {
            updatequestions();
        });
    };

    $scope.deleterequirement = function(id) {
        $http.delete("requirement/" + id).then(function() {
            updaterequirements();
        });
    };

    $scope.deletetool = function(id) {
        $http.delete("tool/" + id).then(function() {
            updatetools();
        });
    };

    $scope.deletedataproduct = function(id) {
        $http.delete("dataproduct/" + id).then(function() {
            updatedataproducts();
        });
    };

    $scope.deletedatasystem = function(id) {
        $http.delete("datasystem/" + id).then(function() {
            updatedatasystems();
        });
    };

    var updatenetworks = function() {
        $http.get("network").success(function(response) {
            $scope.networks = response;
        });
    };

    var updateeovs = function() {
        $http.get("eov").success(function(response) {
            $scope.eovs = response;
        });
    };

    var updatetools = function() {
        $http.get("tool").success(function(response) {
            $scope.tools = response;
        });
    };

    var updatedataproducts = function() {
        $http.get("dataproduct").success(function(response) {
            $scope.dataproducts = response;
        });
    };

    var updatedatasystems = function() {
        $http.get("datasystem").success(function(response) {
            $scope.datasystems = response;
        });
    };

    var updateparents = function() {
        $http.get("parent").success(function(response) {
            $scope.parents = response;
        });
    };

    var updateactivities = function() {
        $http.get("activity").success(function(response) {
            $scope.activities = response;
        });
    };

    var updatequestions = function() {
        $http.get("question").success(function(response) {
            $scope.questions = response;
        });
    };

    var updaterequirements = function() {
        $http.get("requirement").success(function(response) {
            $scope.requirements = response;
        });
    };

    $scope.addtool = function() {
        $scope.activity.tools.push($scope.temp.tool);
    };

    $scope.adddataproduct = function() {
        $scope.activity.dataproducts.push($scope.temp.dataproduct);
    };

    $scope.addquestion = function() {
        $scope.eov.questions.push($scope.temp.question);
    };

    $scope.addrequirement = function() {
        $scope.eov.requirements.push($scope.temp.requirement);
    };

    updatenetworks();
    updateeovs();
    updatetools();
    updatedataproducts();
    updateparents();
    updateactivities();
    updatequestions();
    updaterequirements();

});

app.controller("outputcontroller", function($scope, $http) {

    var updatequestions = function() {
        $http.get("question").success(function(response) {
            $scope.questions = response;
        });
    };

    $scope.getoutput = function() {
        $http.get("output/" + $scope.question.id).success(function(response) {
            $scope.output = response;
        });
    };

    updatequestions();

});

app.controller("mapcontroller", function($scope, $http, $timeout, $document) {

    var updatemap = function() {
        $http.get("geojson").success(function(response) {
            var geojson = response;
            var map = L.map("map").setView([30, 0], 1);
            L.tileLayer("http://server.arcgisonline.com/ArcGIS/rest/services/Ocean_Basemap/MapServer/tile/{z}/{y}/{x}", {}).addTo(map);
            var geo = L.geoJson(geojson, {
                style: {
                    color: "#cc3300",
                    weight: 4,
                    opacity: 1
                }
            }).addTo(map);
        });
    };

    $document.ready(function(){
        updatemap();
    });

});

app.controller("graphcontroller", function($scope, $http, $timeout, $document) {

    $scope.groups = ["eov", "question", "activity", "network", "tool", "dataproduct", "datasystem", "requirement"];
    /*$scope.status = [
        {
            name: "eov",
            checked: true
        },
        {
            name: "question",
            checked: true
        },
        {
            name: "activity",
            checked: true
        },
        {
            name: "network",
            checked: true
        },
        {
            name: "tool",
            checked: true
        },
        {
            name: "dataproduct",
            checked: true
        },
        {
            name: "requirement",
            checked: true
        }
    ];*/

    var creategraph = function(data) {

        var nodes = [];
        var nodenames = [];
        var registry = {};
        var edges = [];

        var addprop = function(row, key) {
            if (row.hasOwnProperty(key) && row[key] != null && row[key] != "") {
                if (nodenames.indexOf(row[key]) < 0) {
                    nodenames.push(row[key]);
                    nodes.push({ name: row[key], group: $scope.groups.indexOf(key) });
                }
            }
        };

        var addrelationship = function(row, first, second) {
            if (row.hasOwnProperty(first) && row.hasOwnProperty(second) && row[first] != null && row[first] != "" && row[second] != null && row[second] != "") {
                var f = nodenames.indexOf(row[first]);
                var s = nodenames.indexOf(row[second]);
                var key = f + "_" + s;
                if (!registry.hasOwnProperty(key)) {
                    registry[key] = true;
                    edges.push({source: f, target: s, value: 1});
                }
            }
        };

        for (var i = 0; i < data.length; i++) {

            var row = data[i];

            // add strings to arrays

            for (var g = 0; g < $scope.groups.length; g++) {
                //if ($scope.status[g].checked == true) {
                    addprop(row, $scope.groups[g]);
                //}
            }

            // extract relationships

            addrelationship(row, "question", "eov");
            addrelationship(row, "requirement", "eov");
            addrelationship(row, "eov", "activity");
            addrelationship(row, "activity", "network");
            addrelationship(row, "activity", "tool");
            addrelationship(row, "activity", "dataproduct");
            addrelationship(row, "activity", "datasystem");

        }

        return { nodes: nodes, links: edges };

    };

    var updategraph = function() {
        $http.get("graphdata").success(function(response) {
            $scope.graph = creategraph(response);
            $scope.drawgraph();
        });
    };

    $scope.drawgraph = function() {

        var graph = $scope.graph;

        var el = d3.select("#graph");
        var width = el.style("width").replace("px", "");
        var height = el.style("height").replace("px", "");

        el.selectAll("svg").remove();

        var svg = el.append("svg")
            .attr("width", width)
            .attr("height", height);

        var color = d3.scale.category20();
        var colors = d3.scale.category20().range();

        var force = d3.layout.force()
            .charge(-120)
            .linkDistance(30)
            .size([width, height]);

        force
            .nodes(graph.nodes)
            .links(graph.links)
            .start();

        var link = svg.selectAll(".link")
            .data(graph.links)
            .enter().append("line")
            .attr("class", "link")
            .style("stroke-width", function(d) { return Math.sqrt(d.value); });

        var tip = d3.tip()
            .attr("class", "d3-tip")
            .offset([-1, 0])
            .html(function (d) {
                if ($scope.groups[d.group] == "network" || $scope.groups[d.group] == "tool" || $scope.groups[d.group] == "question" || $scope.groups[d.group] == "dataproduct" || $scope.groups[d.group] == "datasystem" || $scope.groups[d.group] == "requirement") {
                    return d.name + "";
                } else if ($scope.groups[d.group] == "activity") {
                    return "Activity " + d.name;
                }
            });

        var node = svg.selectAll(".node")
            .data(graph.nodes)
            .enter().append("g")
            .attr("class", "node")
            .call(force.drag)
            .on('mouseover', tip.show)
            .on('mouseout', tip.hide);

        node.append("circle")
            .attr("class", "node")
            .attr("r", 5)
            .style("fill", function(d) {
                return colors[d.group];
            });

        node.append("text")
            .attr("dx", 12)
            .attr("dy", 4)
            .text(function(d) {
                if ($scope.groups[d.group] == "eov") {
                    return d.name;
                }
            })
            .attr("class", "graphlabel");

        force.on("tick", function() {
            link.attr("x1", function(d) { return d.source.x; })
                .attr("y1", function(d) { return d.source.y; })
                .attr("x2", function(d) { return d.target.x; })
                .attr("y2", function(d) { return d.target.y; });

            node.attr("transform", function(d) { return "translate(" + d.x + "," + d.y + ")"; });
        });

        svg.call(tip);

        var ordinal = d3.scale.category20()
            .domain($scope.groups);

        svg.append("g")
            .attr("class", "legendOrdinal")
            .attr("transform", "translate(20,20)");

        var legendOrdinal = d3.legend.color()
            .shape("path", d3.svg.symbol().type("circle").size(100)())
            .shapePadding(10)
            .scale(ordinal);

        svg.select(".legendOrdinal")
            .call(legendOrdinal);

    };

    $document.ready(function(){
        updategraph();
    });

});
