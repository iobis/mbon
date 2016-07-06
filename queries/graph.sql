select array_to_json(array_agg(t)) from
(
select array_agg(a) from
(
select
	question.name as question,
	requirement.name as requirement,
	eov.name as eov,
	activity.id as activity,
	network.name as network,
	tool.name as tool,
	dataproduct.name as dataproduct,
	datasystem.name as datasystem
from goos.eov
left join goos.eov_question eq on eq.eov_id = eov.id
left join goos.question on question.id = eq.question_id
left join goos.requirement_eov er on er.eov_id = eov.id
left join goos.requirement on requirement.id = er.requirement_id
left join goos.activity on activity.eov_id = eov.id
left join goos.network on network.id = activity.network_id
left join goos.activity_tool at on at.activity_id = activity.id
left join goos.tool on at.tool_id = tool.id
left join goos.activity_dataproduct ad on ad.activity_id = activity.id
left join goos.dataproduct on ad.dataproduct_id = dataproduct.id
left join goos.activity_datasystem ads on ads.activity_id = activity.id
left join goos.datasystem on ads.datasystem_id = datasystem.id
where activity.id is not null
) a
) t;