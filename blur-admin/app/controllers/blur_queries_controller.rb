class BlurQueriesController < ApplicationController

  before_filter :current_zookeeper, :only => [:index, :refresh]
  before_filter :zookeepers, :only => :index

  def index

    filters = {}
    filters[:created_at] = (Time.now - 1.minutes)..Time.now
    filters[:running] = true

    @blur_tables = @current_zookeeper.blur_tables

    @blur_queries = BlurQuery.joins(:blur_table => :cluster).
                             where(:blur_table =>{:clusters => {:zookeeper_id => @current_zookeeper.id}}).
                             where(filters).
                             includes(:blur_table).
                             order("created_at DESC")
  end

  def refresh
    filters = {}
    # convert string bools into real bools
    [:super_query_on, :running, :interrupted].each do |category|
      params[category] = true  if params[category] == 'true'
      params[category] = false if params[category] == 'false'
    end
    # filters for columns
    [:blur_table_id, :super_query_on, :running, :interrupted].each do |category|
      filters[category] = params[category] unless params[category] == nil or params[category] == ''
    end
    # filter for time
    now = Time.now
    past_time = params[:created_at_time] ? now - params[:created_at_time].to_i.minutes : now - 1.minutes
    filters[:created_at] = past_time..now

    # filter for refresh period
    unless params[:time_since_refresh].empty?
      previous_filter_time = now - params[:time_since_refresh].to_i.seconds
      filters[:updated_at] = previous_filter_time..now
    end

    # filter by zookeeper
    @blur_queries = BlurQuery.joins(:blur_table => :cluster).
                             where(:blur_table =>{:clusters => {:zookeeper_id => @current_zookeeper.id}}).
                             where(filters).
                             includes(:blur_table).
                             order("created_at DESC")
    respond_to do |format|
      format.html {render @blur_queries}
    end
  end

  def update
    @blur_query = BlurQuery.find params[:id]
    if params[:cancel] == 'true'
      @blur_query.cancel
    end
    respond_to do |format|
      format.html {render :partial => 'blur_query', :locals => { :blur_query => @blur_query }}
    end
  end

  def more_info
    @blur_query = BlurQuery.find(params[:id]).includes(:blur_table)
    respond_to do |format|
      format.html {render :partial => 'more_info', :locals => {:blur_query => @blur_query}}
    end
  end
end
