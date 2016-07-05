select coalesce(array_to_json(array_agg(a)), '[]')
from (
	select
		activity.id,
		coverage,
		timescale,
		(
			select row_to_json(n)
			from (
				select id, name
				from goos.network
				where network.id = activity.network_id
			) n
		) as network,
		(
			select row_to_json(n)
			from (
				select id, name
				from goos.eov
				where eov.id = activity.eov_id
			) n
		) as eov,
		(
			select array_to_json(array_agg(row_to_json(t)))
			from (
				select tool.id, tool.name
				from goos.activity_tool
				left join goos.tool on tool.id = activity_tool.tool_id
				where activity_tool.activity_id = activity.id
			) t
		) as tools
	from goos.activity
) a;
