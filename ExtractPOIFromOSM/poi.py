from imposm.parser import OSMParser as P
from itertools import chain, groupby

nested_nodes = []
p = P(concurrency=4, nodes_callback=lambda x: nested_nodes.append(x))

print 'Loading Los Angeles'
p.parse('los-angeles.osm.pbf')
nodes = list(chain.from_iterable(nested_nodes))
print 'Extracted %d nodes' % len(nodes)

amenities = [(tags, coord) for (id, tags, coord) in nodes if 'amenity' in tags]

sorted_amenities = sorted(amenities, key=lambda (tags,coord): tags['amenity'])
grouped_amenities = groupby(sorted_amenities, lambda (tags, coords): tags['amenity'])
types_of_amenities = [(k, len(list(v))) for (k, v) in grouped_amenities]
types_of_amenities = sorted(types_of_amenities, key=lambda (k, count): count, reverse=True)


grouped_amenities = groupby(sorted_amenities, lambda (tags, coords): tags['amenity'])
amenity_locations = [(k, [coord for (tags, coord) in L]) for (k, L) in grouped_amenities]
amentiy_locations = [(k, L) for (k, L) in amenity_locations if len(L) > 20]



