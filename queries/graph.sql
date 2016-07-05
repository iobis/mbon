select array_to_json(array_agg(t)) from
(
select array_agg(a) from
(
select
	question.name as question,
	eov.name as eov,
	activity.id as activity,
	network.name as network,
	tool.name as tool 
from goos.question
left join goos.eov_question eq on eq.question_id = question.id
left join goos.eov on eov.id = eq.eov_id
left join goos.activity on activity.eov_id = eov.id
left join goos.network on network.id = activity.network_id
left join goos.activity_tool at on at.activity_id = activity.id
left join goos.tool on at.tool_id = tooL.id
) a
) t;