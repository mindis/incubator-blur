class BlurTablesController < ApplicationController

  before_filter :current_zookeeper, :only => :index
  before_filter :zookeepers, :only => :index

  def index
    @blur_tables = @current_zookeeper.blur_tables.order('status DESC, table_name ASC')
  end

  def update
    blur_table = BlurTable.find(params[:id])
    if params[:enable]
      blur_table.enable
    elsif params[:disable]
      blur_table.disable
    end

    respond_to do |format|
      format.html { render :partial => 'blur_table', :locals => {:blur_table => blur_table }}
    end
  end

  def destroy
    blur_table = BlurTable.find(params[:id])
    destroy_index = params[:delete_index] == 'true'
    blur_table.destroy destroy_index

    respond_to do |format|
      format.js  { render :nothing => true }
    end
  end

  def schema
    blur_table = BlurTable.find(params[:id])

    respond_to do |format|
      format.html {render :partial => 'schema', :locals => {:blur_table => blur_table}}
    end
  end

  def hosts
    blur_table = BlurTable.find(params[:id])

    respond_to do |format|
      format.html {render :partial => 'hosts', :locals => {:blur_table => blur_table}}
    end
  end
end
