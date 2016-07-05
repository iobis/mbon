select row_to_json(a)
from (
select 
	id,
	(
		select array_to_json(array_agg(row_to_json(t)))
		from (
			select eov.id, eov.name
			from goos.eov_question eq
			left join goos.eov on eov.id = eq.eov_id
			where eq.question_id = question.id
		) t
	) as eovs,
	(
		select array_to_json(array_agg(row_to_json(t)))
		from (
			select network.id, network.name, network.ongoing
			from goos.eov_question eq
			left join goos.eov on eov.id = eq.eov_id
			left join goos.activity on activity.eov_id = eov.id
			left join goos.network on activity.network_id = network.id
			where eq.question_id = question.id
			group by network.id, network.name, network.ongoing
		) t
	) as networks,
	(
		select array_to_json(array_agg(row_to_json(t)))
		from (
			select
				activity.id,
				ST_AsText(ST_Envelope(activity.coverage)) as coverage,
				network.name as network
			from goos.eov_question eq
			left join goos.eov on eov.id = eq.eov_id
			left join goos.activity on activity.eov_id = eov.id
			left join goos.network on network.id = activity.network_id
			where eq.question_id = question.id
			group by activity.id, activity.coverage, network.name
		) t
	) as activities,
	(
		select array_to_json(array_agg(row_to_json(t)))
		from (
			select tool.id, tool.name
			from goos.eov_question eq
			left join goos.eov on eov.id = eq.eov_id
			left join goos.activity on activity.eov_id = eov.id
			left join goos.activity_tool at on at.activity_id = activity.id
			left join goos.tool on tool.id = at.tool_id
			where eq.question_id = question.id
			group by tool.id, tool.name
		) t
	) as tools
from goos.question
where question.id = 4
) a;