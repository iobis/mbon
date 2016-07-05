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
        questions: []
    };
    $scope.question = {};
    $scope.activity = {
        tools: []
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

    $scope.addtool = function() {
        $scope.activity.tools.push($scope.temp.tool);
    };

    $scope.addquestion = function() {
        $scope.eov.questions.push($scope.temp.question);
    };

    updatenetworks();
    updateeovs();
    updatetools();
    updateparents();
    updateactivities();
    updatequestions();

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

    var creategraph = function(data) {

        var nodes = [];
        var nodenames = [];
        var registry = {};
        var edges = [];
        var groups = ["question", "eov", "activity", "network", "tool"];

        var addprop = function(row, key) {
            if (row.hasOwnProperty(key) && row[key] != null && row[key] != "") {
                if (nodenames.indexOf(row[key]) < 0) {
                    nodenames.push(row[key]);
                    nodes.push({ name: row[key], group: groups.indexOf(key) });
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

            addprop(row, "question");
            addprop(row, "eov");
            addprop(row, "activity");
            addprop(row, "network");
            addprop(row, "tool");

            // extract relationships

            addrelationship(row, "question", "eov");
            addrelationship(row, "eov", "activity");
            addrelationship(row, "activity", "network");
            addrelationship(row, "activity", "tool");

        }

        return { nodes: nodes, links: edges };

    };

    var updategraph = function() {
        $http.get("graphdata").success(function(response) {
            var graph = creategraph(response);
            drawgraph(graph);
        });
    };

    var drawgraph = function(graph) {

        console.log(graph);

        var width = 960,
        height = 500;

        var color = d3.scale.category20();

        var force = d3.layout.force()
            .charge(-120)
            .linkDistance(30)
            .size([width, height]);

        var svg = d3.select("#graph").append("svg")
            .attr("width", width)
            .attr("height", height);

        force
            .nodes(graph.nodes)
            .links(graph.links)
            .start();

        var link = svg.selectAll(".link")
            .data(graph.links)
            .enter().append("line")
            .attr("class", "link")
            .style("stroke-width", function(d) { return Math.sqrt(d.value); });

        var node = svg.selectAll(".node")
            .data(graph.nodes)
            .enter().append("g")
            .attr("class", "node")
            .call(force.drag);

        node.append("circle")
            .attr("class", "node")
            .attr("r", 5)
            .style("fill", function(d) { return color(d.group); });

        node.append("text")
            .attr("dx", 12)
            .attr("dy", ".35em")
            .text(function(d) {
                if (d.group == 1) {
                    return d.name;
                }
            });

        node.append("title")
            .text(function(d) {
                return d.name;
            });

        force.on("tick", function() {
            link.attr("x1", function(d) { return d.source.x; })
                .attr("y1", function(d) { return d.source.y; })
                .attr("x2", function(d) { return d.target.x; })
                .attr("y2", function(d) { return d.target.y; });

            node.attr("transform", function(d) { return "translate(" + d.x + "," + d.y + ")"; });
        });

    };

    $document.ready(function(){
        updategraph();
    });

});
