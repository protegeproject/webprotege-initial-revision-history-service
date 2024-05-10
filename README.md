# webprotege-initial-revision-history-service

This is a WebProtégé microservice that is responsible for generating an intial revision history from an existing ontology.  It pulls a set of ontology documents from a (MinIO) bucket and generates the revision history to create these ontologies from scratch.  It then places the revision history into a new bucket.
